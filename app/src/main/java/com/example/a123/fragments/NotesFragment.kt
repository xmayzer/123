package com.example.a123.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.a123.MainActivity
import com.example.a123.R
import com.example.a123.adapter.NoteAdapter
import com.example.a123.databinding.FragmentNotesBinding
import com.example.a123.model.Note
import androidx.navigation.fragment.findNavController
import com.example.a123.viewmodel.NoteViewModel


class NotesFragment : Fragment(R.layout.fragment_notes) {

    private var noteBinding:FragmentNotesBinding? = null
    private val binding get() = noteBinding!!
    private lateinit var addNoteFab: AppCompatButton

    private lateinit var notesViewModel : NoteViewModel
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        noteBinding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesViewModel = (activity as MainActivity).noteViewModel
        setupNotesRecyclerView()


        addNoteFab = view.findViewById(R.id.addNoteFab)

        // Устанавливаем обработчик нажатия на кнопку
        addNoteFab.setOnClickListener {
            // Переход в AddFragment
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, AddFragment())
            transaction.addToBackStack(null) // Добавляем в стек для возможности возврата
            transaction.commit()
        }
    }

    private fun setupNotesRecyclerView() {
        noteAdapter = NoteAdapter(parentFragmentManager)
        binding.notesRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = noteAdapter
        }
        activity?.let {
            notesViewModel.getAllNotes().observe(viewLifecycleOwner){ note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            }
        }
    }

    private fun updateUI(note: List<Note>?){
        if (note != null){
            if (note.isNotEmpty()){
                binding.emptyNotesImage.visibility = View.GONE
                binding.notesRecyclerView.visibility = View.VISIBLE
            } else{
                binding.emptyNotesImage.visibility = View.VISIBLE
                binding.notesRecyclerView.visibility = View.GONE
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        noteBinding = null
    }

}