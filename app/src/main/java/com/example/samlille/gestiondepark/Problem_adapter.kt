package com.example.samlille.gestiondepark


import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.example.samlille.gestiondepark.DataBase.Problem_Entity
import com.example.samlille.gestiondepark.Services.CusomDataBaseService
import kotlinx.android.synthetic.main.problem_item.view.*
import android.app.Activity


/**
 * Problem_adapter for our recycleView
 */
class Problem_adapter(val items : ArrayList<Problem_Entity>, val context: Context, cusomDataBaseService: CusomDataBaseService) : RecyclerView.Adapter<ViewHolder>() {

    private  var cusomDataBaseService = cusomDataBaseService

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.problem_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.problemType?.text = items.get(position).type
        holder?.peorblemDescription?.text = items.get(position).description
        holder?.problemLocation?.text = items.get(position).location

        holder.itemView.setOnClickListener({
            holder.itemView.context.startActivity(Intent(holder.itemView.context, problemAfficher::class.java)
                    .putExtra("ShowProblem",items.get(position).location)
                    .putExtra("id",items.get(position).uid)
                    .putExtra("problemType", items.get(position).type))
            (context as Activity).finish()

        })

        holder.itemView.setOnLongClickListener({
            showDialogError(holder.itemView.context, items.get(position).uid)

        })
    }


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    fun showDialogError(context: Context, uid: Long?): Boolean {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Supprimer le problème")
        builder.setMessage("Voulez-vous supprimer ce problème?")
        builder.setPositiveButton("Oui"){dialog, which ->
            Toast.makeText(context,"Problem Deleted", Toast.LENGTH_SHORT).show()
            this.cusomDataBaseService.deletById(uid)
            (context as Activity).finish()
            context.startActivity(context.getIntent())

        }
        builder.setNegativeButton("Non"){dialog, which ->
            Toast.makeText(context,"Problem not deleted", Toast.LENGTH_SHORT).show()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        return true
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val problemType = view.titleProblem
    val peorblemDescription = view.descriptionProblem
    val problemLocation = view.locationProblem
}