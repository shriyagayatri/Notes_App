package com.hackathon.notesapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {
    var listNotes=ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LoadQuery("%")
        //listNotes.add(Note(1,"Quote1","The world is never quiet, even its silence eternally resounds with the same notes, in vibrations which escape our ears. As for those that we perceive, they carry sounds to us, occasionally a chord, never a melody."))
        //listNotes.add(Note(2,"Quote2","There's an effort to reclaim the unmentionable, the unsayable, the unspeakable, all those things come into being a composer, into writing music, into searching for notes and pieces of musical information that don't exist."))
        //listNotes.add(Note(3,"Quote3","Learning is the beginning of wealth. Learning is the beginning of health. Learning is the beginning of spirituality. Searching and learning is where the miracle process all begins."))



    }
    override  fun onResume() {
        super.onResume()
        LoadQuery("%")

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onPause() {
        super.onPause()


    }

    override fun onStop() {
        super.onStop()


    }

    override fun onDestroy() {
        super.onDestroy()


    }

    override fun onRestart() {
        super.onRestart()
        
    }
    fun LoadQuery(title:String){



        var dbManager=DbManager(this)
        val projections= arrayOf("ID","Title","Description")
        val selectionArgs= arrayOf(title)
        val cursor=dbManager.Query(projections,"Title like ?",selectionArgs,"Title")
        listNotes.clear()
        if(cursor.moveToFirst()){

            do{
                val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val Title=cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID,Title,Description))

            }while (cursor.moveToNext())
        }

        var myNotesAdapter=MyNotesAdapter(this,listNotes)
        lvNotes.adapter=myNotesAdapter


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val sv=menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext,query,Toast.LENGTH_LONG).show()
                LoadQuery("%"+query+"%")
                return false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when(item.itemId){
                R.id.addNote->{
                    var intent=Intent(this,Add_notes::class.java)
                    startActivity(intent)

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    inner class MyNotesAdapter:BaseAdapter{
        var context:Context?=null
        var listNotesAdapter=ArrayList<Note>()
        constructor(context: Context,listNotesAdapter: ArrayList<Note>):super(){
            this.listNotesAdapter=listNotesAdapter
            this.context=context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView=layoutInflater.inflate(R.layout.ticket,null)
            var myNote=listNotesAdapter[position]
            myView.tvTitle.text=myNote.noteName
            myView.tvDes.text=myNote.noteDes
            myView.ivDelete.setOnClickListener{
                var dbManager=DbManager(this.context!!)
                val selectionArgs= arrayOf(myNote.noteId.toString())
                dbManager.Delete("ID=?",selectionArgs)
                LoadQuery("%")
            }
            myView.ivEdit.setOnClickListener{

                GoToUpdate(myNote)

            }
            return myView


        }

        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }

    }
    fun GoToUpdate(note:Note){
        var intent=  Intent(this,Add_notes::class.java)
        intent.putExtra("ID",note.noteId)
        intent.putExtra("name",note.noteName)
        intent.putExtra("des",note.noteDes)
        startActivity(intent)
    }

}
