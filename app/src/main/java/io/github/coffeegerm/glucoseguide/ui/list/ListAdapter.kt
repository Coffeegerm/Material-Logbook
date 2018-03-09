/*
 * Copyright 2017 Coffee and Cream Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.coffeegerm.glucoseguide.ui.list

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.data.model.EntryItem
import io.github.coffeegerm.glucoseguide.ui.entry.EditEntryActivity
import io.github.coffeegerm.glucoseguide.utils.Constants
import io.github.coffeegerm.glucoseguide.utils.DateFormatter
import io.github.coffeegerm.glucoseguide.utils.SharedPreferenceManager
import javax.inject.Inject

class ListAdapter internal constructor(var context: Context) : RecyclerView.Adapter<ListViewHolder>() {
  
  @Inject
  lateinit var sharedPreferencesManager: SharedPreferenceManager
  @Inject
  lateinit var resources: Resources
  @Inject
  lateinit var dateFormatter: DateFormatter
  
  private lateinit var item: EntryItem
  private var inflater: LayoutInflater
  private var entryItemList: List<EntryItem>? = null
  
  init {
    GlucoseGuide.syringe.inject(this)
    inflater = LayoutInflater.from(context)
  }
  
  fun setListItems(providedList: List<EntryItem>) {
    this.entryItemList = providedList
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder = ListViewHolder(inflater.inflate(R.layout.item_list, parent, false))
  
  override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
    item = entryItemList!![holder.adapterPosition]
    
    holder.date.text = dateFormatter.formatDate(item.date)
    
    if (sharedPreferencesManager.getBoolean(Constants.MILITARY_TIME)) {
      holder.time.text = item.date?.let { dateFormatter.twentyFourHourFormat(it) }
    } else {
      holder.time.text = item.date?.let { dateFormatter.twelveHourFormat(it) }
    }
    
    holder.entryView.setOnClickListener {
      Toast.makeText(context, R.string.list_item_short_click, Toast.LENGTH_SHORT).show()
    }
    
    holder.entryView.setOnLongClickListener {
      val editEntryActivity = Intent(context, EditEntryActivity::class.java)
      val selectedItem = entryItemList!![holder.adapterPosition]
      editEntryActivity.putExtra(Constants.ITEM_ID, selectedItem.id)
      context.startActivity(editEntryActivity)
      true
    }
    
    if (item.bloodGlucose.toString() == "0")
      holder.bloodGlucose.setText(R.string.dash)
    else
      holder.bloodGlucose.text = item.bloodGlucose.toString()
    
    if (item.carbohydrates.toString() == "0")
      holder.carbohydrates.setText(R.string.dash)
    else
      holder.carbohydrates.text = item.carbohydrates.toString()
    
    if (item.insulin.toString() == "0.0")
      holder.insulin.setText(R.string.dash)
    else
      holder.insulin.text = item.insulin.toString()
    
    if (item.status > 0) {
      holder.statusImage.visibility = View.VISIBLE
      when (item.status) {
        1 -> holder.statusImage.setImageDrawable(resources.getDrawable(R.drawable.breakfast))
        2 -> holder.statusImage.setImageDrawable(resources.getDrawable(R.drawable.lunch))
        3 -> holder.statusImage.setImageDrawable(resources.getDrawable(R.drawable.dinner))
        4 -> holder.statusImage.setImageDrawable(resources.getDrawable(R.drawable.sweets))
        5 -> holder.statusImage.setImageDrawable(resources.getDrawable(R.drawable.sick))
        6 -> holder.statusImage.setImageDrawable(resources.getDrawable(R.drawable.exercise))
      }
    }
  }
  
  override fun getItemCount(): Int = entryItemList!!.size
}
