/*
 * Copyright 2018 Coffee and Cream Studios
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

package io.github.coffeegerm.glucoseguide.ui.statistics.children

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.coffeegerm.glucoseguide.GlucoseGuide.Companion.syringe
import io.github.coffeegerm.glucoseguide.R
import io.github.coffeegerm.glucoseguide.data.DatabaseManager
import io.github.coffeegerm.glucoseguide.ui.statistics.StatisticsViewModel
import io.github.coffeegerm.glucoseguide.ui.statistics.StatisticsViewModelFactory
import kotlinx.android.synthetic.main.fragment_all_stats.*
import javax.inject.Inject

/**
 * Fragment used with Statistics ViewPager to show
 * the amount of all statistics
 */

class AllStatisticsFragment : Fragment() {
  
  @Inject
  lateinit var databaseManager: DatabaseManager
  
  @Inject
  lateinit var statisticsViewModelFactory: StatisticsViewModelFactory
  lateinit var statisticsViewModel: StatisticsViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    syringe.inject(this)
    statisticsViewModel = ViewModelProviders.of(this, statisticsViewModelFactory).get(StatisticsViewModel::class.java)
  }
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_all_stats, container, false)
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    if (databaseManager.getAllSortedDescending().isNotEmpty()) {
      all_days_statistics_average.text = statisticsViewModel.getAverage()
      highest_of_all_glucose.text = databaseManager.getHighestBloodGlucose().toString()
      lowest_of_all_glucose.text = databaseManager.getLowestBloodGlucose().toString()
    }
  }
}
