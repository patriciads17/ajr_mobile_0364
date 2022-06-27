package com.p3l.ajr_mobile_0364.manager;

import static com.android.volley.Request.Method.GET;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import com.p3l.ajr_mobile_0364.adapter.TopCustomerReportAdapter;
import com.p3l.ajr_mobile_0364.adapter.TopDriverReportAdapter;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardActivity extends AppCompatActivity {

    private static final String[] MONTH = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private UserPreferences userPreferences;
    private SwipeRefreshLayout srCust, srDriver;
    private TopCustomerReportAdapter topCustomerReportAdapter;
    private TopDriverReportAdapter topDriverReportAdapter;
    private RequestQueue queue;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout layoutLoading;
    private Button btn1,btn2;
    private AutoCompleteTextView tfData1;
    private RecyclerView rvCust, rvDriver;
    private List<Report> reportListCust, reportListDriver;

    private BottomNavigationView.OnNavigationItemSelectedListener navigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.nav_home){
                startActivity(new Intent(LeaderboardActivity.this, ManagerActivity.class));
            }else if(item.getItemId() == R.id.nav_order){
                startActivity(new Intent(LeaderboardActivity.this, LeaderboardActivity.class));
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);
        queue = Volley.newRequestQueue(this);
        layoutLoading = findViewById(R.id.layout_loading);
        userPreferences = new UserPreferences(LeaderboardActivity.this);
        srCust = findViewById(R.id.sr_cust);
        srDriver = findViewById(R.id.sr_driver);
        btn1 = findViewById(R.id.btn_go);
        btn2 = findViewById(R.id.btn_print);
        tfData1 = findViewById(R.id.tf_month);

        btn2.setVisibility(View.INVISIBLE);

        ArrayAdapter<String> adapterMonth =
                new ArrayAdapter<>(this, R.layout.item_list, MONTH);
        tfData1.setAdapter(adapterMonth);

        if(!userPreferences.checkLogin()) {
            startActivity(new Intent(LeaderboardActivity.this, LoginActivity.class));
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

        srCust.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkMonth();
            }
        });

        srDriver.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkMonth();
            }
        });

        rvCust = findViewById(R.id.rv_top_cust);
        topCustomerReportAdapter = new TopCustomerReportAdapter(new ArrayList<>(), this);
        rvCust.setLayoutManager(new LinearLayoutManager(LeaderboardActivity.this, LinearLayoutManager.VERTICAL, false));
        rvCust.setAdapter(topCustomerReportAdapter);

        rvDriver = findViewById(R.id.rv_top_driver);
        topDriverReportAdapter = new TopDriverReportAdapter(new ArrayList<>(), this);
        rvDriver.setLayoutManager(new LinearLayoutManager(LeaderboardActivity.this, LinearLayoutManager.VERTICAL, false));
        rvDriver.setAdapter(topDriverReportAdapter);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    cetakPdfCust();
                    cetakPdfDriver();
                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTopCustReport(String month, String token, String token_type){
        srCust.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(GET, Api.GET_TOP_CUST_REPORT + month, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ReportResponse reportResponse = gson.fromJson(response, ReportResponse.class);
                reportListCust = reportResponse.getReportList();
                topCustomerReportAdapter.cleanRecyview();
                topCustomerReportAdapter.setReportList(reportResponse.getReportList());
                Toast.makeText(LeaderboardActivity.this, reportResponse.getMessage(), Toast.LENGTH_SHORT).show();
                srCust.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                srCust.setRefreshing(false);
                try {
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(LeaderboardActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(LeaderboardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getTopDriverReport(String month, String token, String token_type){
        srDriver.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(GET, Api.GET_TOP_DRIVER_REPORT + month, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ReportResponse reportResponse = gson.fromJson(response, ReportResponse.class);
                reportListDriver = reportResponse.getReportList();
                topDriverReportAdapter.cleanRecyview();
                topDriverReportAdapter.setReportList(reportResponse.getReportList());
                Toast.makeText(LeaderboardActivity.this, reportResponse.getMessage(), Toast.LENGTH_SHORT).show();
                srDriver.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                srDriver.setRefreshing(false);
                try {
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(LeaderboardActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(LeaderboardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            getTopCustReport("1",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getTopDriverReport("1",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Feb")){
            getTopCustReport("2",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getTopDriverReport("2",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Mar")){
            getTopCustReport("3",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getTopDriverReport("3",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Apr")){
            getTopCustReport("4",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getTopDriverReport("4",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("May")){
            getTopCustReport("5",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getTopDriverReport("5",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Jun")){
            getTopCustReport("6",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getTopDriverReport("6",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Jul")){
            getTopCustReport("7",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getTopDriverReport("7",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Aug")){
            getTopCustReport("8",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getTopDriverReport("8",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Sep")){
            getTopCustReport("9",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getTopDriverReport("9",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Oct")){
            getTopCustReport("10",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getTopDriverReport("10",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Nov")){
            getTopCustReport("11",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getTopDriverReport("11",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }else if(tfData1.getText().toString().equalsIgnoreCase("Dec")){
            getTopCustReport("12",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getTopDriverReport("12",userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }
    }

    private void cetakPdfCust() throws FileNotFoundException, DocumentException {
        File folder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!folder.exists()) {
            folder.mkdir();
        }
        Date currentTime = Calendar.getInstance().getTime();
        String pdfName = "AJR | TOP 5 Customer" + ".pdf";
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
        Paragraph judul3 = new Paragraph("List of 5 Customers with the Most Number of Transactions in a Certain Month \n\n",
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
        Paragraph Pembuka = new Paragraph("\nSorted by the number of transactions per month. \n\n", f);
        Pembuka.setIndentationLeft(20);
        document.add(Pembuka);
        PdfPTable tableHeader = new PdfPTable(new float[]{5, 5, 5});

        tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        tableHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableHeader.getDefaultCell().setFixedHeight(30);
        tableHeader.setTotalWidth(PageSize.A4.getWidth());
        tableHeader.setWidthPercentage(100);
        // Setup Column
        PdfPCell h1 = new PdfPCell(new Phrase("Customer ID"));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h1.setPaddingBottom(5);
        PdfPCell h2 = new PdfPCell(new Phrase("Customer Name"));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPaddingBottom(5);
        PdfPCell h3 = new PdfPCell(new Phrase("Amount of Transaction"));
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setPaddingBottom(5);
        tableHeader.addCell(h1);
        tableHeader.addCell(h2);
        tableHeader.addCell(h3);
        // Beri warna untuk kolumn
        for (PdfPCell cells : tableHeader.getRow(0).getCells()) {
            cells.setBackgroundColor(BaseColor.BLUE);
        }
        document.add(tableHeader);
        PdfPTable tableData = new PdfPTable(new float[]{5, 5, 5});

        tableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableData.getDefaultCell().setFixedHeight(30);
        tableData.setTotalWidth(PageSize.A4.getWidth());
        tableData.setWidthPercentage(100);
        tableData.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        for (Report R : reportListCust) {
            tableData.addCell(R.getId());
            tableData.addCell(R.getNama_customer());
            tableData.addCell(R.getJumlah_transaksi());
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

    private void cetakPdfDriver() throws FileNotFoundException, DocumentException {
        File folder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (!folder.exists()) {
            folder.mkdir();
        }
        Date currentTime = Calendar.getInstance().getTime();
        String pdfName = "AJR | TOP 5 Driver" + ".pdf";
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
        Paragraph judul3 = new Paragraph("List of 5 Drivers with the Most Number of Transactions in a Certain Month \n\n",
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
        Paragraph Pembuka = new Paragraph("\nSorted by the number of transactions per month. \n\n", f);
        Pembuka.setIndentationLeft(20);
        document.add(Pembuka);
        PdfPTable tableHeader = new PdfPTable(new float[]{5, 5, 5});

        tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        tableHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableHeader.getDefaultCell().setFixedHeight(30);
        tableHeader.setTotalWidth(PageSize.A4.getWidth());
        tableHeader.setWidthPercentage(100);
        // Setup Column
        PdfPCell h1 = new PdfPCell(new Phrase("Driver Id"));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h1.setPaddingBottom(5);
        PdfPCell h2 = new PdfPCell(new Phrase("Driver Name"));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPaddingBottom(5);
        PdfPCell h3 = new PdfPCell(new Phrase("Amount of Transaction"));
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setPaddingBottom(5);
        tableHeader.addCell(h1);
        tableHeader.addCell(h2);
        tableHeader.addCell(h3);
        // Beri warna untuk kolumn
        for (PdfPCell cells : tableHeader.getRow(0).getCells()) {
            cells.setBackgroundColor(BaseColor.BLUE);
        }
        document.add(tableHeader);
        PdfPTable tableData = new PdfPTable(new float[]{5, 5, 5});

        tableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableData.getDefaultCell().setFixedHeight(30);
        tableData.setTotalWidth(PageSize.A4.getWidth());
        tableData.setWidthPercentage(100);
        tableData.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        for (Report R : reportListDriver) {
            tableData.addCell(R.getId());
            tableData.addCell(R.getNama_driver());
            tableData.addCell(R.getJumlah_transaksi());
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