package com.example.primefitness.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.primefitness.databinding.FragmentImportExportDatabaseBinding
import com.example.primefitness.global.DB
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel


class FragmentImportExportDatabase : Fragment() {
    private lateinit var binding: FragmentImportExportDatabaseBinding
    private var db:DB?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImportExportDatabaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Import Export Database"
        db = activity?.let { DB(it) }

//        fillOldMobile()

        binding.btnImportDatabase.setOnClickListener {
            try {
                val ieDB = ImportExportDB()
                ieDB.createDirectory()
                ieDB.importDB()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.btnExportDatabase.setOnClickListener {
            try {
                val ieDB = ImportExportDB()
                ieDB.exportDB()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }


    private fun showToast(value: String){
        Toast.makeText(activity, value, Toast.LENGTH_LONG).show()
    }


    class ImportExportDB : Activity(){
        fun createDirectory(){
            val direct =
                File(Environment.getExternalStorageDirectory().toString() + "/Exam Creator")

            if (!direct.exists()) {
                if (direct.mkdir()) {
                    //directory is created;
                }
            }
        }
        @SuppressLint("SdCardPath")
        fun importDB(){
            createDirectory()
            try {
                val sd = Environment.getExternalStorageDirectory()
                val data = Environment.getDataDirectory()
                if (sd.canWrite()) {
                    val  currentDBPath = "/data/" + "com.example.primefitness" + "/databases/" + "PrimeFitness.DB";
                    val backupDBPath  = "/PrimeFitness/PrimeFitness.DB";
                    val  backupDB = File(data, currentDBPath)
                    val currentDB  = File(sd, backupDBPath)
                    val src : FileChannel = FileInputStream(currentDB).channel
                    val dst : FileChannel = FileOutputStream(backupDB).channel
                    dst.transferFrom(src, 0, src.size());
                    src.close()
                    dst.close()
                    Toast.makeText(baseContext, backupDB.toString(), Toast.LENGTH_LONG).show()
                }
        } catch (e: Exception) {
            e.printStackTrace()
            }
        }

        @SuppressLint("SdCardPath")
        fun exportDB(){
            createDirectory()
            try {
                val sd = Environment.getExternalStorageDirectory()
                val data = Environment.getDataDirectory()
                if (sd.canWrite()) {
                    val currentDBPath = ("/data/" + "com.example.primefitness" + "/databases/" + "PrimeFitness.DB")
                    val backupDBPath = "/PrimeFitness/PrimeFitness.DB"
                    val currentDB = File(data, currentDBPath)
                    val backupDB = File(sd, backupDBPath)
                    val src = FileInputStream(currentDB).channel
                    val dst = FileOutputStream(backupDB).channel
                    dst.transferFrom(src, 0, src.size())
                    src.close()
                    dst.close()
                    Toast.makeText(baseContext, backupDB.toString(), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(baseContext, e.toString(), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}