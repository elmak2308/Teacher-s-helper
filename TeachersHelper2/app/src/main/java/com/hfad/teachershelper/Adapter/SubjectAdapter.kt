package com.hfad.teachershelper.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hfad.teachershelper.R
import com.hfad.teachershelper.databinding.ListItemBinding
import com.hfad.teachershelper.retrofit.Subject

class SubjectAdapter : ListAdapter<Subject, SubjectAdapter.Holder>(Comparator()) {

//    class Holder(view: View) : RecyclerView.ViewHolder(view){
//        private val binding = ListItemBinding.bind(view)
//
//        fun bind(subject: Subject) {
//            binding.button.text = subject.name
//        }
//
//
//    }
      class Holder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
          fun bind(subject: Subject) {
              binding.button.text = subject.name
          }
      }

    class Comparator : DiffUtil.ItemCallback<Subject>(){
        override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
            return oldItem.id == newItem.id

        }
        override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
            return oldItem == newItem

        }

    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.list_item, parent, false)
//        return Holder(view)
//
//    }
      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
          val binding = ListItemBinding.inflate(
              LayoutInflater.from(parent.context),
              parent,
              false
          )
          return Holder(binding)
      }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}