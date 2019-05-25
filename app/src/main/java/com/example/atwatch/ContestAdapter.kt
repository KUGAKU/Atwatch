package com.example.atwatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class ContestAdapter(data: OrderedRealmCollection<Contest>) :
        RealmRecyclerViewAdapter<Contest, ContestAdapter.ViewHolder>(data, true){

    init {
        setHasStableIds(true)
    }

    class ViewHolder(cell: View) : RecyclerView.ViewHolder(cell) {
        val title : TextView = cell.findViewById(android.R.id.text1)
    }


    override fun onBindViewHolder(holder: ContestAdapter.ViewHolder, position: Int) {
        val contest: Contest? = getItem(position)
        holder.title.text = contest?.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContestAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_2,parent,false)
        return ViewHolder(view)
    }
}