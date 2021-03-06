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

package io.github.coffeegerm.glucoseguide.dagger

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import io.github.coffeegerm.glucoseguide.data.DatabaseManager
import io.github.coffeegerm.glucoseguide.data.RealmTransactions
import io.github.coffeegerm.glucoseguide.utils.DateAssistant
import io.github.coffeegerm.glucoseguide.utils.SharedPreferencesManager

@Module
class DataModule {
  
  @Provides
  fun providesDatabaseManager(sharedPreferences: SharedPreferences) = DatabaseManager(RealmTransactions(), DateAssistant(), SharedPreferencesManager(sharedPreferences))
  
}