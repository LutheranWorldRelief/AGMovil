package kronos.cacaomovil.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kronos.cacaomovil.R
import kronos.cacaomovil.models.BibliotecaM
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import android.R.attr.button
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.widget.*
import kronos.cacaomovil.Constants
import kronos.cacaomovil.activities.ListadoGuias


class AdapterBiblioteca(private val mContext: Activity, private val listCategories: ArrayList<BibliotecaM>) : RecyclerView.Adapter<AdapterBiblioteca.MyViewHolder>(), Filterable {

    private var listCategoriesFiltered: MutableList<BibliotecaM> = ArrayList<BibliotecaM>()

    init {
        listCategoriesFiltered = listCategories
    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listCategoriesFiltered = results!!.values as MutableList<BibliotecaM>

                // refresh the list with filtered data
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charSearch = constraint.toString().trim()
                charSearch = Constants.remove(charSearch)

                System.out.println("charSearch "+charSearch+"...")

                if (charSearch.isEmpty()){

                    listCategoriesFiltered = listCategories
                } else {
                    var listCategoriesClone: MutableList<BibliotecaM> = ArrayList<BibliotecaM>()

                    for (row in listCategories) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.nameFind.toLowerCase().contains(charSearch.toLowerCase()) || row.descriptionFind.toLowerCase().contains(charSearch.toLowerCase())) {
                            listCategoriesClone.add(row)
                        }
                    }

                    listCategoriesFiltered = listCategoriesClone
                }

                val filterResults = FilterResults()
                filterResults.values = listCategoriesFiltered
                return filterResults
            }

        }
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image_favorite: CircleImageView? = null
        var txName: TextView? = null
        var txDescripcion: TextView? = null
        var rlCont:RelativeLayout? = null

        init {
            image_favorite = view.findViewById(R.id.image_favorite) as CircleImageView
            txName = view.findViewById(R.id.txName) as TextView
            txDescripcion = view.findViewById(R.id.txDescripcion) as TextView
            rlCont = view.findViewById(R.id.rlCont) as RelativeLayout

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.items_categoria, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val categories = listCategoriesFiltered[position]

        holder.txName!!.setText(categories.name)
        holder.txDescripcion!!.setText(categories.description)

        Picasso.get()
                .load(categories.image)
                .placeholder(R.drawable.web_hi)
                .error(R.drawable.web_hi)
                .into(holder.image_favorite)


        holder.rlCont!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val i = Intent(mContext, ListadoGuias::class.java)
                i.putExtra("id", categories.id)
                i.putExtra("name", categories.name)
                mContext.startActivity(i)
            }
        })
    }

    override fun getItemCount(): Int {
        return listCategoriesFiltered.size
    }
}
