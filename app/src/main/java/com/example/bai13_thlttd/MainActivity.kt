package com.example.bai13_thlttd

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bai13_thlttd.databinding.ActivityMainBinding
import com.example.bai13_thlttd.model.Student
import com.example.bai13_thlttd.utils.gone
import com.example.bai13_thlttd.utils.show
import com.example.bai13_thlttd.utils.toAcademicRanking
import com.example.bai13_thlttd.utils.toast
import com.example.bai13_thlttd.utils.trimmedText

class MainActivity : AppCompatActivity() {

    // =====================================================
    // VIEW BINDING
    // =====================================================

    private lateinit var binding: ActivityMainBinding


    // =====================================================
    // SINH VIÊN MẶC ĐỊNH
    // =====================================================

    private val defaultStudent = Student(
        id = "2415053122326",
        name = "Võ Thị Phương Linh",
        className = "24T3",
        email = "2415053122326@sv.te.udn.vn",
        gpa = 3.75
    )


    // Sinh viên hiện tại
    private var currentStudent = defaultStudent


    // =====================================================
    // ON CREATE
    // =====================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        restoreStudentState(savedInstanceState)
        bindStudentData(currentStudent)
        setupListeners()
    }
    private fun bindStudentData(student: Student) {
        with(binding) {
            tvStudentName.text =
                student.name
            tvStudentDetails.text =
                getString(
                    R.string.student_id_class,
                    student.id,
                    student.className
                )
            tvStudentEmail.text =
                getString(
                    R.string.student_email,
                    student.email
                )
            tvGpaBadge.text =
                getString(
                    R.string.student_gpa,
                    student.gpa,
                    student.gpa.toAcademicRanking()
                )
            edtGpaInput.setText(
                formatGpa(student.gpa)
            )
            if (student.isHonorStudent) {
                tvHonorStudent.show()
            } else {
                tvHonorStudent.gone()
            }
        }
    }

    private fun setupListeners() {
        binding.btnUpdateGpa.setOnClickListener {
            updateStudentGpa()
        }
        binding.btnReset.setOnClickListener {
            resetStudent()
        }
    }
    private fun updateStudentGpa() {
        binding.edtGpaInput.error = null
        val input =
            binding.edtGpaInput.trimmedText()
        if (input.isEmpty()) {
            binding.edtGpaInput.error =
                getString(
                    R.string.error_empty_gpa
                )
            binding.edtGpaInput.requestFocus()
            return
        }
        val normalizedInput =
            input.replace(",", ".")
        val newGpa =
            normalizedInput.toDoubleOrNull()
        if (newGpa == null) {
            binding.edtGpaInput.error =
                getString(
                    R.string.error_invalid_gpa
                )
            binding.edtGpaInput.requestFocus()
            return
        }
        if (newGpa !in MIN_GPA..MAX_GPA) {
            binding.edtGpaInput.error =
                getString(
                    R.string.error_gpa_range
                )
            binding.edtGpaInput.requestFocus()
            return
        }
        if (newGpa == currentStudent.gpa) {
            toast(
                getString(
                    R.string.gpa_no_change
                )
            )
            return
        }
        currentStudent =
            currentStudent.copy(
                gpa = newGpa
            )
        bindStudentData(currentStudent)
        binding.edtGpaInput.clearFocus()
        toast(
            getString(
                R.string.update_success
            )
        )
    }
    private fun resetStudent() {
        currentStudent =
            currentStudent.copy(
                gpa = defaultStudent.gpa
            )
        bindStudentData(currentStudent)
        binding.edtGpaInput.error = null
        toast(
            getString(
                R.string.reset_success
            )
        )
    }

    private fun formatGpa(gpa: Double): String {

        return if (gpa % 1.0 == 0.0) {

            String.format("%.1f", gpa)

        } else {

            gpa.toString()
        }
    }


    // =====================================================
    // LƯU STATE KHI ACTIVITY SẮP BỊ HỦY
    // =====================================================

    override fun onSaveInstanceState(
        outState: Bundle
    ) {

        super.onSaveInstanceState(outState)


        /**
         * Student implement Serializable
         * nên lưu được trực tiếp vào Bundle.
         */
        outState.putSerializable(
            KEY_STUDENT,
            currentStudent
        )
    }


    // =====================================================
    // KHÔI PHỤC STATE
    // =====================================================

    private fun restoreStudentState(
        savedInstanceState: Bundle?
    ) {

        // Nếu Activity mở lần đầu thì không có Bundle
        if (savedInstanceState == null) {

            return
        }


        val savedStudent: Student? =

            /**
             * Android 13 trở lên.
             */
            if (
                Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.TIRAMISU
            ) {

                savedInstanceState.getSerializable(
                    KEY_STUDENT,
                    Student::class.java
                )

            } else {

                /**
                 * Android cũ.
                 */
                @Suppress("DEPRECATION")

                savedInstanceState.getSerializable(
                    KEY_STUDENT
                ) as? Student
            }


        // Nếu lấy được Student thì khôi phục
        savedStudent?.let {

            currentStudent = it
        }
    }


    // =====================================================
    // CONSTANT
    // =====================================================

    companion object {

        private const val KEY_STUDENT =
            "KEY_STUDENT"

        private const val MIN_GPA =
            0.0

        private const val MAX_GPA =
            4.0
    }
}