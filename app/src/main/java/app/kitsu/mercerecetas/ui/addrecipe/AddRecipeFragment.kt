package app.kitsu.mercerecetas.ui.addrecipe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import app.kitsu.mercerecetas.database.RecipeDatabase
import app.kitsu.mercerecetas.databinding.FragmentAddRecipeBinding
import kotlinx.android.synthetic.main.fragment_add_recipe.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddRecipeFragment : Fragment() {

    private lateinit var addRecipeViewModel: AddRecipeViewModel
    private lateinit var binding: FragmentAddRecipeBinding

    lateinit var currentPhotoPath: String
    val REQUEST_TAKE_PHOTO = 1


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Get a reference to the binding object and inflate the fragment views.
        binding = FragmentAddRecipeBinding.inflate(inflater,container, false)

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = RecipeDatabase.getInstance(application).recipeDatabaseDao

        val dashboardViewModelFactory = AddRecipeViewModelFactory(dataSource,application)

        addRecipeViewModel =
               ViewModelProvider(this, dashboardViewModelFactory).get(AddRecipeViewModel::class.java)

        binding.addRecipeViewModel = addRecipeViewModel

        binding.RadioGroupMode.setOnClickListener {
            if(it != null) {
                closeKeyBoard()
            }
        }

        binding.recipeTypeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            closeKeyBoard()
        }


        binding.recipeModeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            closeKeyBoard()
        }

        binding.editTextTime.setOnEditorActionListener { v, actionId, event ->
            if (actionId == KeyEvent.KEYCODE_ENDCALL) {
                closeKeyBoard()
                true
            }
           else false
        }

        binding.takePhotoFAB.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.createButton.setOnClickListener {
            addRecipeViewModel.createRecipe(binding.root)
            addRecipeViewModel.onRecipeCreated()
        }

        addRecipeViewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            if (it){ this.findNavController().navigate(AddRecipeFragmentDirections.actionNavigationAddrecipeToNavigationHome())
                addRecipeViewModel.displayHomeComplete()
            }
        })

        return binding.root;
    }


    private fun closeKeyBoard() {
        val view = this.activity?.currentFocus
        if (view != null) {
            val imm = this.activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }



    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }



    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Toast.makeText(context,"Not able to create image file",Toast.LENGTH_LONG).show()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context!!,
                        "app.kitsu.mercerecetas.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)

                }

            }
        }
    }
    fun setImageFromUri() {

            // Get the dimensions of the View
            val targetW: Int = recipeImageView.width
            val targetH: Int = recipeImageView.height

            val bmOptions = BitmapFactory.Options().apply {
                // Get the dimensions of the bitmap
                inJustDecodeBounds = true

                val photoW: Int = outWidth
                val photoH: Int = outHeight

                // Determine how much to scale down the image
                val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

                // Decode the image file into a Bitmap sized to fill the View
                inJustDecodeBounds = false
                inSampleSize = scaleFactor

            }
            BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
                recipeImageView.setImageBitmap(bitmap)
                Toast.makeText(context,"Imagen guardada correctamente",Toast.LENGTH_SHORT).show()
            }

        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === requestCode && resultCode == Activity.RESULT_OK) {
            setImageFromUri()
            recipeImageView.hideText()
        }
    }

    private fun View.hideText(){
        visibility = View.INVISIBLE
    }
}


