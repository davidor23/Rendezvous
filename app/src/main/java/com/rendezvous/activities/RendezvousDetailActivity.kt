package com.rendezvous.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rendezvous.R
import com.rendezvous.models.RendezvousModel
import kotlinx.android.synthetic.main.activity_rendezvous_detail.*

class RendezvousDetailActivity : AppCompatActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_rendezvous_detail)

        var rendezvousDetailModel: RendezvousModel? = null

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            // get the Serializable data model class with the details in it
            rendezvousDetailModel =
                    intent.getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as RendezvousModel
        }

        if (rendezvousDetailModel != null) {

            setSupportActionBar(toolbar_rendezvous_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = rendezvousDetailModel.title

            toolbar_rendezvous_detail.setNavigationOnClickListener {
                onBackPressed()
            }

            iv_place_image.setImageURI(Uri.parse(rendezvousDetailModel.image))
            tv_description.text = rendezvousDetailModel.description
            tv_location.text = rendezvousDetailModel.location
        }

        btn_view_on_map.setOnClickListener {
            val intent = Intent(this@RendezvousDetailActivity, MapActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, rendezvousDetailModel)
            startActivity(intent)
        }
    }
}