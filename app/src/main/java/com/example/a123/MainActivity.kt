package com.example.a123

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.a123.database.NoteDatabase
import androidx.navigation.fragment.findNavController
import com.example.a123.fragments.GraphFragment
import com.example.a123.fragments.LightFragment
import com.example.a123.fragments.NotesFragment
import com.example.a123.repository.NoteRepository
import com.example.a123.viewmodel.NoteViewModel
import com.example.a123.viewmodel.NoteViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val notesFragment = NotesFragment()
        val graphFragment = GraphFragment()
        val lightFragment = LightFragment()

        makeCurrentFragment(notesFragment)

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.baseline_assignment -> makeCurrentFragment(notesFragment)
                R.id.baseline_lightbulb -> makeCurrentFragment(lightFragment)
                R.id.baseline_auto_graph -> makeCurrentFragment(graphFragment)
            }
            true
        }

        setupViewModel()
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            commit()
        }
    private fun setupViewModel(){
        val noteRepository = NoteRepository(NoteDatabase(this))
        val viewModelProviderFactory = NoteViewModelFactory(application, noteRepository)
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory)[NoteViewModel::class.java]
    }
}