package com.example.a123.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.a123.MainActivity
import com.example.a123.R
import androidx.navigation.fragment.findNavController
import com.example.a123.databinding.FragmentEditBinding
import com.example.a123.model.Note
import com.example.a123.viewmodel.NoteViewModel

class EditFragment : Fragment(R.layout.fragment_edit) {
    private var editNoteBinding: FragmentEditBinding? = null
    private val binding get() = editNoteBinding!!
    private lateinit var notesViewModel: NoteViewModel
    private lateinit var currentNote: Note

    private var selectedMoodImageId: Int = -1
    private lateinit var moodImages: Array<ImageView>

    private val args: EditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editNoteBinding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewModel = (activity as MainActivity).noteViewModel
        currentNote = args.note!!

// Инициализация массива изображений настроений
        moodImages = arrayOf(
            binding.imageStar1,
            binding.imageStar2,
            binding.imageStar3,
            binding.imageStar4,
            binding.imageStar5
        )

        highlightSelectedImage(currentNote.moodImageId)

        binding.editNoteTitle.setText(currentNote.noteTitle)
        binding.editNoteDesc.setText(currentNote.noteDesc)
        // Устанавливаем обработчики кликов для картинок настроений
        moodImages.forEachIndexed { index, imageView ->
            val moodImageRes = resources.getIdentifier("star${index + 1}", "drawable", context?.packageName)
            imageView.setImageResource(moodImageRes)
            imageView.setOnClickListener {
                selectedMoodImageId = moodImageRes // Обновляем выбранное изображение
                highlightSelectedImage(index) // Выделяем выбранное изображение
            }

            // Выделение картинки, если она соответствует текущему настроению
            if (currentNote.moodImageId == moodImageRes) {
                selectedMoodImageId = moodImageRes // Присваиваем выбранное изображение
                highlightSelectedImage(index) // Выделяем соответствующее изображение
            }
        }
        binding.buttonSaveEdit.setOnClickListener {
            val noteTitle = binding.editNoteTitle.text.toString().trim()
            val noteDesc = binding.editNoteDesc.text.toString().trim()

            if(noteTitle.isNotEmpty()){
                val note = Note(currentNote.id, noteTitle, noteDesc, selectedMoodImageId, date  = currentNote.date)
                notesViewModel.updateNote(note)
                val transaction = parentFragmentManager.beginTransaction()

                transaction.replace(R.id.fragmentContainerView, NotesFragment())
                transaction.addToBackStack(null) // Добавляем в стек для возможности возврата
                transaction.commit()
            } else{
                Toast.makeText(context, "Введите название", Toast.LENGTH_SHORT).show()
            }
        }
        binding.buttonUndoEdit.setOnClickListener {
            deleteNote() // Вызываем метод для удаления заметки
        }
    }
    private fun highlightSelectedImage(moodImageId: Int) {
        for (imageView in moodImages) {
            imageView.setAlpha(0.5f) // Устанавливаем прозрачность для невыбранных изображений
        }

        val selectedImage = moodImages.getOrNull(moodImageId)
        selectedImage?.setAlpha(1.0f) // Устанавливаем полную непрозрачность для выбранного изображения
    }
    private fun deleteNote(){
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Удалить запись")
            setMessage("Вы уверены, что желаете удалить запись?")
            setPositiveButton("Удалить"){_,_ ->
                notesViewModel.deleteNote(currentNote)
                Toast.makeText(context, "Запись удалена", Toast.LENGTH_SHORT).show()

                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, NotesFragment())
                transaction.addToBackStack(null) // Добавляем в стек для возможности возврата
                transaction.commit()
            }
            setNegativeButton("Закрыть", null)
        }.create().show()
    }
}