package com.example.yoon_android_bykotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.yoon_android_bykotlin.databinding.ActivityIntroBinding


class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private val TAG = this::class.java.simpleName

    // 권한 요청 승인 코드 번호
    val PERMISSION_REQUEST_CODE = 1000

    // 앱 권한 리스트
    val permissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,   // 위치 정보 - gps랑 네트워크를 모두 사용함. - 더 정확함.
        Manifest.permission.ACCESS_COARSE_LOCATION, // 위치 정보 - 네트워크만 사용함.
        Manifest.permission.READ_CONTACTS,          // 연락처
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.READ_EXTERNAL_STORAGE,  // 외부저장소(사진,파일)
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view) // 뷰 바인딩 적용 완료
    }

    public override fun onResume() {
        super.onResume()  // Always call the superclass method first

        /**
         * 앱 권한 체크
         */
        if (checkPermission()) { // 이미 앱권한 모두 얻은 경우
            val handler = Handler()
            handler.postDelayed({
                val intent = Intent(this, MainActivity::class.java) // 이동화면 변경
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }, 1000)// 1초 딜레이 중
        } else {    // 앱권한 얻어야 함
            requestPermissions(permissionList, PERMISSION_REQUEST_CODE)
        }

    }


    /**
     * 앱 권한 체크 함수
     */
    private fun checkPermission(): Boolean {
        for (permissionIndex in permissionList) {
            // 권한 허용 여부를 확인한다.
            val chk = checkCallingOrSelfPermission(permissionIndex)

            if (chk == PackageManager.PERMISSION_GRANTED) {
                // 허용
            } else if (chk == PackageManager.PERMISSION_DENIED) {
                // 거부
                return false // false 리턴하고 바로 빠져나옴
            }
        }
        return true
    }


    /**
     * 권한 요청 결과
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                for (idx in grantResults.indices) {
                    if (grantResults[idx] == PackageManager.PERMISSION_GRANTED) {
                        // DoNothing
                    } else if (grantResults[idx] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "권한을 설정해주세요.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

    }

}