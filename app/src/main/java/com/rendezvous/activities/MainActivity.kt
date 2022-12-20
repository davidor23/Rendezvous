package com.rendezvous.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rendezvous.R
import com.rendezvous.adapters.RendezvousAdapter
import com.rendezvous.database.DatabaseHandler
import com.rendezvous.models.RendezvousModel
import com.rendezvous.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_main.*
import pl.kitek.rvswipetodelete.SwipeToEditCallback

class MainActivity : AppCompatActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)

        // This is used to align the xml view to this class
        setContentView(R.layout.activity_main)

        // Setting an click event for Fab Button and calling the AddRendezvousActivity.
        fabAddRendezvous.setOnClickListener {
            val intent = Intent(this@MainActivity, AddRendezvousActivity::class.java)

            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }

        getRendezvousListFromLocalDB()
    }

    // Call Back method  to get the Message form other Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check if the request code is same as what is passed  here it is 'ADD_PLACE_ACTIVITY_REQUEST_CODE'
        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getRendezvousListFromLocalDB()
            }else{
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }

    /**
     * A function to get the list of happy place from local database.
     */
    private fun getRendezvousListFromLocalDB() {

        val dbHandler = DatabaseHandler(this)

        val getRendezvousList = dbHandler.getRendezvousList()

        if (getRendezvousList.size > 0) {
            rv_rendezvous_list.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE
            setupRendezvousRecyclerView(getRendezvousList)
        } else {
            rv_rendezvous_list.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE
        }
    }

    /**
     * A function to populate the recyclerview to the UI.
     */
    private fun setupRendezvousRecyclerView(rendezvoussList: ArrayList<RendezvousModel>) {

        rv_rendezvous_list.layoutManager = LinearLayoutManager(this)
        rv_rendezvous_list.setHasFixedSize(true)

        val placesAdapter = RendezvousAdapter(this, rendezvoussList)
        rv_rendezvous_list.adapter = placesAdapter

        placesAdapter.setOnClickListener(object :
            RendezvousAdapter.OnClickListener {
            override fun onClick(position: Int, model: RendezvousModel) {
                val intent = Intent(this@MainActivity, RendezvousDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model) // Passing the complete serializable data class to the detail activity using intent.
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_rendezvous_list.adapter as RendezvousAdapter
                adapter.notifyEditItem(
                        this@MainActivity,
                        viewHolder.adapterPosition,
                        ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_rendezvous_list)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_rendezvous_list.adapter as RendezvousAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getRendezvousListFromLocalDB() // Gets the latest list from the local database after item being delete from it.
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_rendezvous_list)
    }

    companion object{
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        internal const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}