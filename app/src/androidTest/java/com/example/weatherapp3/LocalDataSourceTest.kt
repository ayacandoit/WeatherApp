
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherapp3.data.LocalDataSource.AppDatabase
import com.example.weatherapp3.data.LocalDataSource.FavoriteDao
import com.example.weatherapp3.data.LocalDataSource.LocalDataSourceImpl
import com.example.weatherapp3.data.models.FavoriteLocation

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class LocalDataSourceTest {
    private lateinit var LocalDataSource: LocalDataSourceImpl
    private lateinit var favouritesDAO: FavoriteDao
    private lateinit var database: AppDatabase

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        favouritesDAO = database.favoriteDao()
        LocalDataSource = LocalDataSourceImpl(favouritesDAO)
    }

    @After
    fun cleanup(){
        database.close()
    }

    @Test
    fun getAllLoactions_retrieveLocations() = runTest {
        //Given
        val list = listOf(
             FavoriteLocation(1,"egypt",32.7,33.3),
                     FavoriteLocation(2,"egypt",32.7,33.3)
            ,             FavoriteLocation(3,"egypt",32.7,33.3)




        )

        list.forEach { fav ->
            LocalDataSource.insertLocation(fav)
        }

        //When
        val result = LocalDataSource.getAllLocations().first()

        //Then
        assertThat(result.isNotEmpty(),`is`(true))
}




    @Test
    fun deleteLoc_retrieveAllLocationWithoutFav() = runBlocking {
        //Given
       val fav= FavoriteLocation(1,"egypt",32.7,33.3)
        LocalDataSource.insertLocation(fav)

        //When
        LocalDataSource.deleteLocation(fav)
        val result = LocalDataSource.getAllLocations().first()

        //Then
        assertThat(result.isEmpty(),`is`(true))
    }
}