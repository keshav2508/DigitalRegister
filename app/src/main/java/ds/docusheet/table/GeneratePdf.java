package ds.docusheet.table;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeneratePdf {
    public Boolean PdfCreation(Resources resources, List<SavedData> slist, List<ColumnData> clist, String name, String doc_id, ByteArrayOutputStream stream) throws IOException {
        String filname = name + doc_id;
        String filecontent = "Contentido";
        String d2 = "All Registers in one app";
        String d = "Download App";
        if (Creation(d,d2,resources, filname, filecontent, slist, clist, name, stream))
            return true;
        else return false;
    }

    public Boolean Creation(String d,String d2,Resources resources, String filename, String filecontent, List<SavedData> slist, List<ColumnData> clist, String name, ByteArrayOutputStream stream) throws IOException {
        // create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 500, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.capture);
        Bitmap b = Bitmap.createScaledBitmap(bitmap, 28, 28, true);
        canvas.drawBitmap(b, 60, 20, null);
        paint.setColor(Color.BLUE);
        paint.setTextSize(18);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Digital Register", 90, 42, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(12);
        canvas.drawText(name, 130, 75, paint);

        int left = 80, right = 30, top = 100, bottom = 90;
        int temp = 0, temp2 = 0;

        if (clist.size() == 3) {
            left = 110;
            right = 40;
            top = 100;
            bottom = 90;
        }
        else
        if(clist.size()==4){
        left=90;
        right=30;
        top=100;
        bottom=90;
        }
        else
        if(clist.size()==5){
            left=80;
            right=32;
            top=100;
            bottom=90;
        }
        else
        if(clist.size()==6){
            left=65;
            right=25;
            top=100;
            bottom=90;
        }
        else
        if(clist.size()==7){
            left=55;
            right=20;
            top=100;
            bottom=90;
        }
        else
        if(clist.size()==8||clist.size()==9){
            left=45;
            right=15;
            top=100;
            bottom=90;
        }


        for (int i = 0; i < clist.size(); i++) {
            paint.setColor(resources.getColor(R.color.line));
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.STROKE);
            Rect rect = new Rect(left + temp, top, right + temp2, bottom);
            canvas.drawRect(rect, paint);
            paint.setColor(resources.getColor(R.color.top));
            paint.setStyle(Paint.Style.FILL);
            rect = new Rect(left + temp, top, right + temp2, bottom);
            canvas.drawRect(rect, paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(5);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText(clist.get(i).getColumn_names(), right + temp2 + 3, top - 4, paint);
            temp = temp + left - right;
            temp2 = temp2 + left - right;
        }

        if (clist.size() == 3)
        {
            left = 110;
        right = 40;
        top = 110;
        bottom = 100;
    }
        else if(clist.size()==4)
        {
            left=90;
            right=30;
            top=110;
            bottom=100;
        }
        else if(clist.size()==5){
            left=80;
            right=32;
            top=110;
            bottom=100;
        }
        else
        if(clist.size()==6){
            left=65;
            right=25;
            top=110;
            bottom=100;
        }
        else
        if(clist.size()==7){
            left=55;
            right=20;
            top=110;
            bottom=100;
        }
        else
        if(clist.size()==8||clist.size()==9){
            left=45;
            right=15;
            top=110;
            bottom=100;
        }
        else
        {
            left=80;
            right=30;
            top=110;
            bottom=100;
        }
        int temp3 = 0, temp4 = 0;
        temp=0;temp2=0;

        int page_num=slist.size()/38;
        for (int i = 0; i < slist.size(); i++) {
            // canvas.drawText(sometext,right+temp2+3,top+temp3-4 , paint);
            //temp = temp + left - right;
            //temp2 = temp2 + left - right;
            if (!slist.get(i).getColumn1().equals("null")) {

                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn1(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn2().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn2(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn3().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn3(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn4().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn4(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn5().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn5(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;
                }
                if (!slist.get(i).getColumn6().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn6(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;
                }
                if (!slist.get(i).getColumn7().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn7(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn8().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn8(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn9().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn9(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn10().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn10(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn11().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn11(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn12().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn12(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn13().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn13(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn14().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn14(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }

                if (!slist.get(i).getColumn15().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn15(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn16().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn16(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn17().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn17(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn18().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn18(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;

                }
                if (!slist.get(i).getColumn19().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn19(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;
                }
                if (!slist.get(i).getColumn20().equals("null")) {
                    paint.setColor(resources.getColor(R.color.line));
                    paint.setStrokeWidth(1);
                    paint.setStyle(Paint.Style.STROKE);
                    Rect rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                    canvas.drawRect(rect, paint);
                    if (i == slist.size() - 1) {
                        paint.setColor(resources.getColor(R.color.top));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    } else {
                        paint.setColor(resources.getColor(R.color.white));
                        paint.setStyle(Paint.Style.FILL);
                        rect = new Rect(left + temp, top + temp3, right + temp2, bottom + temp4);
                        canvas.drawRect(rect, paint);
                    }
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(4);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(slist.get(i).getColumn20(), right + temp2 + 3, top + temp3 - 4, paint);
                    temp = temp + left - right;
                    temp2 = temp2 + left - right;
                }


            temp=0;temp2=0;
            temp3 = temp3 + top - bottom;
            temp4 = temp4 + top - bottom;
        }
        document.finishPage(page);
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Download/";

        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + filename+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            document.close();
            return true;
        } catch (IOException e) {
            document.close();
        return false;
        }
        // close the document


    }
}
