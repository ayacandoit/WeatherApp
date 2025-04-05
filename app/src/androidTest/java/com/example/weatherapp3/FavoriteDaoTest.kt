package com.example.weatherapp3

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapp3.data.LocalDataSource.AppDatabase
import com.example.weatherapp3.data.LocalDataSource.FavoriteDao
import com.example.weatherapp3.data.models.FavoriteLocation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class FavouritesDAOTest {
    private lateinit var database: AppDatabase
    private lateinit var favouritesDAO: FavoriteDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java).build()
        favouritesDAO = database.favoriteDao()
    }

    @After
    fun cleanup(){
        database.close()
    }

    @Test
    fun insertLocation_getAllLocationsWithInsertedLocation() = runTest{
        //Given
        val fav  = FavoriteLocation(1,"egypt",32.7,33.3)
        favouritesDAO.insertLocation(fav)

        //When
        val result = favouritesDAO.getAllLocations().first()

        //Then
        assertThat(result.isNotEmpty(),`is`(true))
        assertThat(result.last(),`is`(fav))
    }

    @Test
    fun deleteLocation_getAllLocationsWithoutInsertedLocation() = runTest {
        // Given
        val firstFavourite =FavoriteLocation(1,"egypt",32.7,33.3)
        val secondFavourite =FavoriteLocation(2,"egypt",32.7,33.3)
        favouritesDAO.insertLocation(firstFavourite)
        favouritesDAO.insertLocation(secondFavourite)

        // When
        favouritesDAO.deleteLocation(firstFavourite)
        val result = favouritesDAO.getAllLocations().first()

        // Then
       assertThat(result.isNotEmpty(), `is`(true))
        assertThat(result.contains(firstFavourite), not(`is`(true)))
        assertThat(result.first(), `is`(secondFavourite))
    }
}