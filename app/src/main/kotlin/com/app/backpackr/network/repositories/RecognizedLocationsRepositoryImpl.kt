package com.app.backpackr.network.repositories

import android.content.Context
import com.app.backpackr.BackPackRApp
import com.app.backpackr.network.models.Place
import com.app.backpackr.network.models.dto.LocationDTO
import com.app.backpackr.dagger.components.AppComponent
import io.realm.Realm
import io.realm.RealmResults
import rx.Observable
import javax.inject.Inject

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */

class RecognizedLocationsRepositoryImpl(var context: Context) : RecognizedLocationsRepository {
    var realmComponent: AppComponent
    @Inject lateinit var realmDatabase: Realm

    init {
        realmComponent = BackPackRApp.appComponent
        realmComponent.inject(this)
    }

    override fun addLocation(title: String, country: String, city: String, location: LocationDTO, address: String): Observable<Place> {
        return Observable.create<Place> {
            realmDatabase.beginTransaction()
            val newPlace = Place()
            newPlace.title = title
            newPlace.country = country
            newPlace.city = city
            newPlace.lat = location.latitude
            newPlace.long = location.longitude
            newPlace.address = address
            realmDatabase.copyFromRealm(newPlace)
            realmDatabase.commitTransaction()
        }
    }

    override fun removePlace(place: Place) {
    }

    override fun updatePlace(place: Place) {
    }

    override fun recognizedLocations(): Observable<RealmResults<Place>> {
        return realmDatabase.where(Place::class.java).findAllAsync().asObservable()
    }
}