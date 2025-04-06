package com.example.weatherapp3

import com.example.weatherapp3.data.repository.FavoriteRepository
import junit.framework.TestCase.assertEquals
import org.mockito.ArgumentMatchers.any

// FavoriteRepositoryTest.kt

import com.example.weatherapp3.data.LocalDataSource.LocalDataSource
import com.example.weatherapp3.data.models.FavoriteLocation
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class FavoriteRepositoryTest {

    private lateinit var fakeDataSource: FakeLocalDataSource
    private lateinit var repository: FavoriteRepository

    @Before
    fun setup() {
        fakeDataSource = FakeLocalDataSource()
        repository = FavoriteRepository(fakeDataSource)
    }

    @Test
    fun `addLocation should delegate to data source`() = runTest {
        val testLocation = FavoriteLocation(name = "Giza", latitude = 29.0, longitude = 31.2)

        repository.addLocation(testLocation)

        val locations = fakeDataSource.getAllLocations().first()
        assertTrue(locations.contains(testLocation))
    }

    @Test
    fun `removeLocation should delegate to data source`() = runTest {
        val testLocation = FavoriteLocation(name = "Luxor", latitude = 25.7, longitude = 32.6)
        fakeDataSource.insertLocation(testLocation)

        repository.removeLocation(testLocation)

        val locations = fakeDataSource.getAllLocations().first()
        assertFalse(locations.contains(testLocation))
    }
}