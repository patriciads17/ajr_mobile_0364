package com.p3l.ajr_mobile_0364.manager;

import static com.android.volley.Request.Method.GET;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.p3l.ajr_mobile_0364.LoginActivity;
import com.p3l.ajr_mobile_0364.R;
import com.p3l.ajr_mobile_0364.adapter.CarReportAdapter;
import com.p3l.ajr_mobile_0364.adapter.DriverReportAdapter;
import com.p3l.ajr_mobile_0364.api.Api;
import com.p3l.ajr_mobile_0364.model.Report;
import com.p3l.ajr_mobile_0364.model.response.ReportResponse;
import com.p3l.ajr_mobile_0364.preference.UserPreferences;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CarReportActivity extends AppCompatActivity {
    private static final String[] MONTH = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private UserPreferences userPreferences;
    private SwipeRefreshLayout srCar;
    private CarReportAdapter carReportAdapter;
    private RequestQueue queue;
    private Integer month;
    private LinearLayout layoutLoading;
    private Button btn1,btn2;
    private AutoCompleteTextView tfData1;
    private List<Report> reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_report);

        queue = Volley.newRequestQueue(this);
        layoutLoading = findViewById(R.id.layout_loading);
        userPreferences = new UserPreferences(CarReportActivity.this);
        srCar = findViewById(R.id.sr_car);
        btn1 = findViewById(R.id.btn_go);
        btn2 = findViewById(R.id.btn_print);
        tfData1 = findViewById(R.id.tf_month);

        btn2.setVisibility(View.INVISIBLE);

        ArrayAdapter<String> adapterMonth =
                new ArrayAdapter<>(this, R.layout.item_list, MONTH);
        tfData1.setAdapter(adapterMonth);

        if(!userPreferences.checkLogin()) {
            startActivity(new Intent(CarReportActivity.this, LoginActivity.class));
            finish();
        }

        tfData1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn2.setVisibility(View.INVISIBLE);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn2.setVisibility(View.VISIBLE);
                checkMonth();
            }
        });

        srCar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkMonth();
            }
        });


        RecyclerView rvCar = findViewById(R.id.rv_car_report);
        carReportAdapter = new CarReportAdapter(new ArrayList<>(), this);
        rvCar.setLayoutManager(new LinearLayoutManager(CarReportActivity.this, LinearLayoutManager.VERTICAL, false));
        rvCar.setAdapter(carReportAdapter);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cetakPdf();
                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getCarReport(String month, String token, String token_type){
        srCar.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(GET, Api.GET_CAR_REPORT + month, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ReportResponse reportResponse = gson.fromJson(response, ReportResponse.class);
                reportList = reportResponse.getReportList();
                carReportAdapter.cleanRecyview();
                carReportAdapter.setReportList(reportResponse.getReportList());
                Toast.makeText(CarReportActivity.this, reportResponse.getMessage(), Toast.LENGTH_SHORT).show();
                srCar.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                srCar.setRefreshing(false);
                try {
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(CarReportActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(CarReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", token_type+" "+token);
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        queue.add(stringRequest);
    }

    private void checkMonth(){
        if(tfData1.getText().toString().equalsIgnoreCase("Jan")){
            getCarReport("1",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Feb")){
            getCarReport("2",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Mar")){
            getCarReport("3",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Apr")){
            getCarReport("4",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("May")){
            getCarReport("5",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Jun")){
            getCarReport("6",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Jul")){
            getCarReport("7",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Aug")){
            getCarReport("8",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Sep")){
            getCarReport("9",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Oct")){
            getCarReport("10",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Nov")){
            getCarReport("11",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Dec")){
            getCarReport("12",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }
    }

    private void cetakPdf() throws FileNotFoundException, DocumentException {
        File folder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!folder.exists()) {
            folder.mkdir();
        }
        Date currentTime = Calendar.getInstance().getTime();
        String pdfName = "AJR | Car Rental Report" + ".pdf";
        File pdfFile = new File(folder.getAbsolutePath(), pdfName);
        OutputStream outputStream = new FileOutputStream(pdfFile);
        com.itextpdf.text.Document document = new
                com.itextpdf.text.Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();
        // bagian header
        Paragraph judul1 = new Paragraph("Atma Jogja Rental",
                new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 16,
                        Font.BOLD, BaseColor.BLACK));
        judul1.setAlignment(Element.ALIGN_CENTER);
        document.add(judul1);
        Paragraph judul2 = new Paragraph("-----------------------------------------------------------------------------------------------------",
                new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 12,
                        Font.NORMAL, BaseColor.BLACK));
        judul2.setAlignment(Element.ALIGN_CENTER);
        document.add(judul2);
        Paragraph judul3 = new Paragraph("Car Rental Reports in Specific Months \n\n",
                new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 12,
                        Font.NORMAL, BaseColor.BLACK));
        judul3.setAlignment(Element.ALIGN_CENTER);
        document.add(judul3);
        // Buat tabel
        PdfPTable tables = new PdfPTable(new float[]{16, 8});
        // Settingan ukuran tabel
        tables.getDefaultCell().setFixedHeight(50);
        tables.setTotalWidth(PageSize.A4.getWidth());
        tables.setWidthPercentage(100);
        tables.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell cellSupplier = new PdfPCell();
        cellSupplier.setPaddingLeft(20);
        cellSupplier.setPaddingBottom(10);
        cellSupplier.setBorder(Rectangle.NO_BORDER);
        Paragraph bulan = new Paragraph("Month: " + tfData1.getText().toString().trim() + "\n",
                new com.itextpdf.text.Font(Font.FontFamily.TIMES_ROMAN, 12,
                        Font.NORMAL, BaseColor.BLACK));
        cellSupplier.addElement(bulan);
        tables.addCell(cellSupplier);
        document.add(bulan);
        com.itextpdf.text.Font f = new

                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12,
                com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
        Paragraph Pembuka = new Paragraph("\nSorted by the number of rents per month. \n\n", f);
        Pembuka.setIndentationLeft(20);
        document.add(Pembuka);
        PdfPTable tableHeader = new PdfPTable(new float[]{5, 5, 5, 5});

        tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        tableHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableHeader.getDefaultCell().setFixedHeight(30);
        tableHeader.setTotalWidth(PageSize.A4.getWidth());
        tableHeader.setWidthPercentage(100);
        // Setup Column
        PdfPCell h1 = new PdfPCell(new Phrase("Car Type"));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h1.setPaddingBottom(5);
        PdfPCell h2 = new PdfPCell(new Phrase("Car Name"));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPaddingBottom(5);
        PdfPCell h3 = new PdfPCell(new Phrase("Amount of Rent"));
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setPaddingBottom(5);
        PdfPCell h4 = new PdfPCell(new Phrase("Income"));
        h4.setHorizontalAlignment(Element.ALIGN_CENTER);
        h4.setPaddingBottom(5);
        tableHeader.addCell(h1);
        tableHeader.addCell(h2);
        tableHeader.addCell(h3);
        tableHeader.addCell(h4);
        // Beri warna untuk kolumn
        for (PdfPCell cells : tableHeader.getRow(0).getCells()) {
            cells.setBackgroundColor(BaseColor.BLUE);
        }
        document.add(tableHeader);
        PdfPTable tableData = new PdfPTable(new float[]{5, 5, 5, 5});

        tableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableData.getDefaultCell().setFixedHeight(30);
        tableData.setTotalWidth(PageSize.A4.getWidth());
        tableData.setWidthPercentage(100);
        tableData.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        for (Report R : reportList) {
            tableData.addCell(R.getTipe_mobil());
            tableData.addCell(R.getNama_mobil());
            tableData.addCell(R.getJumlah_peminjaman());
            tableData.addCell(R.getJumlah_pendapatan());
        }
        document.add(tableData);
        com.itextpdf.text.Font h = new

                com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10,
                com.itextpdf.text.Font.NORMAL);
        String tglDicetak = currentTime.toLocaleString();
        Paragraph P = new Paragraph("\nPrinted on " + tglDicetak, h);
        P.setAlignment(Element.ALIGN_RIGHT);
        document.add(P);
        document.close();
        Toast.makeText(this, "PDF created successfully!", Toast.LENGTH_SHORT).show();
    }
}