package com.ddonging.debugtools.utils;



import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *  切记需要增加这个   android:requestLegacyExternalStorage="true"
 */
public class ExcelUtils {
    public static void Export(String filePath, String[] title, List<Map<String, String>> objList, Context context) {

        ExcelUtils.initExcel(filePath, objList.size(), title);
        ExcelUtils.writeObjListToExcel(objList, filePath, context);

    }

    private static WritableFont arial14font = null;

    private static WritableCellFormat arial14format = null;
    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";
    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
    private static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(Colour.VERY_LIGHT_YELLOW);

            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 10);
            arial12format = new WritableCellFormat(arial12font);
            //对齐格式
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            //设置边框
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化Excel表格
     *
     * @param filePath  存放excel文件的路径（path/demo.xls）
     * @param sheetName Excel表格的表名
     * @param colName   excel中包含的列名（可以有多个）
     */
    public static void initExcel(String filePath, int sheetName, String[] colName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                return;
            }
            workbook = Workbook.createWorkbook(file);

            Log.e("TAG", "22222222222222: "+sheetName );
            int size = sheetName / 60000;
            if ((sheetName % 60000) > 0) {
                size += 1;
            }

            Log.e("TAG", "dddASdasd: " +size);
            for (int i = size; i > 0; i--) {

                Log.e("TAG", "initExcel11111111: "+i );
                //设置表格的名字
                WritableSheet sheet = workbook.createSheet("NO" + (i + 1), i);
                //创建标题栏
                sheet.addCell((WritableCell) new Label(0, 0, filePath, arial14format));
                for (int col = 0; col < colName.length; col++) {
                    sheet.addCell(new Label(col, 0, colName[col], arial10format));
                }
                //设置行高
                sheet.setRowView(0, 340);

                Log.e("TAG", "initExcel: "+i );
            }

            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * 将制定类型的List写入Excel中
     *
     * @param objList  待写入的list
     * @param fileName
     * @param c
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> void writeObjListToExcel(List<Map<String, String>> objList, String fileName, final Context c) {
        if (objList != null && objList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);

                in = new FileInputStream(new File(fileName));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName), workbook);

                for (int j = 0; j < objList.size(); j++) {


                    Log.e("excel", "initExcel: " + j / 60000);
                    WritableSheet sheet = writebook.getSheet(j / 60000);
                    sheet.setName("NO." + ((j / 60000) + 1));
                    List<String> list = new ArrayList<>();

                    Map<String, String> map = objList.get(j);

                    for (String s : map.values()) {
                        list.add(s);
                    }

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(i, j - (j / 60000 * 60000) + 1, list.get(i), arial12format));
                        if (list.get(i).length() <= 4) {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 8);
                        } else {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 5);
                        }
                    }
                    //设置行高
                    sheet.setRowView(j - (j / 60000 * 60000) + 1, 350);
                }


                writebook.write();
                workbook.close();
                ((AppCompatActivity) c).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                     ToastUtil.Toast(c,"导出成功");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }


}
