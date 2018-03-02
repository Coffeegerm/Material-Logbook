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

package io.github.coffeegerm.glucoseguide.utils

import android.content.SharedPreferences
import io.github.coffeegerm.glucoseguide.GlucoseGuide
import io.github.coffeegerm.glucoseguide.data.DatabaseManager
import javax.inject.Inject

class Utilities {
  
  init {
    GlucoseGuide.syringe.inject(this)
  }
  
  @Inject
  lateinit var sharedPreferences: SharedPreferences
  @Inject
  lateinit var dateAssistant: DateAssistant
  @Inject
  lateinit var databaseManager: DatabaseManager
  
  fun checkTimeString(hourOfDay: Int, minute: Int): String {
    var hour = hourOfDay
    val timeSet: String
    val min: String = if (minute < 10)
      "0$minute"
    else
      minute.toString()
    when {
      hour > 12 -> {
        hour -= 12
        timeSet = "PM"
      }
      hour == 0 -> {
        hour += 12
        timeSet = "AM"
      }
      hour == 12 -> timeSet = "PM"
      else -> timeSet = "AM"
    }
    return hour.toString() + ":" + min + " " + timeSet
  }
  
  fun formatDate(month: Int, day: Int, year: Int): StringBuilder = StringBuilder().append(month).append("/").append(day).append("/").append(year)
  
  // Calculates the glucose grade based on user
  // sugar from last three days
  fun getGlucoseGrade(): String {
    val grade: String
    val hyperglycemicIndex = sharedPreferences.getInt("hyperglycemicIndex", 0)
    val entriesFromLastThreeDays = databaseManager.getAllFromDate(dateAssistant.getThreeDaysAgoDate())
    val hyperglycemicCount = entriesFromLastThreeDays.indices
          .map { entriesFromLastThreeDays[it]!! }
          .count { it.bloodGlucose > hyperglycemicIndex }
    
    when {
      hyperglycemicCount == 0 -> grade = "-"
      hyperglycemicCount <= 3 -> grade = "A+"
      hyperglycemicCount == 4 -> grade = "A"
      hyperglycemicCount == 5 -> grade = "A-"
      hyperglycemicCount == 6 -> grade = "B+"
      hyperglycemicCount == 7 -> grade = "B"
      hyperglycemicCount == 8 -> grade = "B-"
      hyperglycemicCount == 9 -> grade = "C+"
      hyperglycemicCount == 10 -> grade = "C"
      hyperglycemicCount == 11 -> grade = "C-"
      hyperglycemicCount == 12 -> grade = "D+"
      hyperglycemicCount == 13 -> grade = "D"
      hyperglycemicCount == 14 -> grade = "D-"
      else -> grade = "F"
    }
    
    return grade
  }
  
}