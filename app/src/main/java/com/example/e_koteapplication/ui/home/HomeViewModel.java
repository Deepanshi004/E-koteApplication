package com.example.e_koteapplication.ui.home;

import android.text.Html;
import android.text.Spanned;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> text1;
    private final MutableLiveData<Spanned> text2;  // Use Spanned for HTML support

    public HomeViewModel() {
        text1 = new MutableLiveData<>();
        text1.setValue("Welcome to E-Kote Application");

        text2 = new MutableLiveData<>();
        // Use Html.fromHtml properly and store the result as Spanned
        text2.setValue(Html.fromHtml(
                "The <b>ultimate weapon management app</b> designed specifically for the <b>Indian Defence Force Services</b>.<br/><br/>" +
                        "With <b>barcode-powered tracking</b>, <b>secure access</b>, and <b>real-time monitoring</b>, managing weapons has never been easier.<br/><br/>" +
                        "<b>Why Choose E-KOTE?</b><br/><br/>" +
                        "<b>✅ Quick Scan &amp; Track</b><br/>Instantly retrieve weapon details with barcode scanning. Keep a full history of each weapon with date and time stamps.<br/><br/>" +
                        "<b>\uD83D\uDD12 Enhanced Security</b><br/>Automates record-keeping to ensure only authorized personnel can access weapons, maintaining accountability and security at all times.<br/><br/>" +
                        "<b>⚡ Effortless Management</b><br/>Streamline weapon issuance, returns, and maintenance scheduling, allowing personnel to focus on their duties while E-KOTE manages the backend tasks.<br/><br/>" +
                        "<b>Key Features:</b><br/><br/>" +
                        "• <b>Instant Weapon Tracking:</b> Easily track each weapon's history and real-time data using barcode scanning.<br/><br/>" +
                        "• <b>Secure Access:</b> Only authorized personnel can issue or return weapons, ensuring security.<br/><br/>" +
                        "• <b>Smart Management:</b> Simplify administrative tasks, from issuance to returns, and track maintenance schedules.<br/><br/>" +
                        "<b>\uD83D\uDCF2 Smart. Secure. Seamless.</b><br/>Experience the future of <b>weapon inventory control</b> with <b>E-KOTE</b>.<br/><br/>" +
                        "<b>\uD83D\uDE80 Download Now &amp; Simplify Weapon Management!</b>",
                Html.FROM_HTML_MODE_LEGACY  // Use legacy mode for full HTML support
        ));
    }

    public LiveData<String> getText1() {
        return text1;
    }

    public LiveData<Spanned> getText2() {  // Change return type to Spanned
        return text2;
    }
}
