package com.example.samlille.gestiondepark


import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.samlille.gestiondepark.DataBase.Problem_Entity
import kotlinx.android.synthetic.main.problem_item.view.*

class Problem_adapter(val items : ArrayList<Problem_Entity>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.problem_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.problemType?.text = items.get(position).type
        holder?.peorblemDescription?.text = items.get(position).description
        holder?.problemLocation?.text = items.get(position).location

        holder.itemView.setOnClickListener({
            holder.itemView.context.startActivity(Intent(holder.itemView.context, map2Activity::class.java))

        })
    }


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val problemType = view.titleProblem
    val peorblemDescription = view.descriptionProblem
    val problemLocation = view.locationProblem
}