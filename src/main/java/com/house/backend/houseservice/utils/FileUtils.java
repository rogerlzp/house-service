
package com.house.backend.houseservice.utils;

import com.house.backend.houseservice.enums.EncodingEnum;
import com.house.backend.houseservice.exception.HouseException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Helen.Chen
 */
@Slf4j
public class FileUtils {



    /**
     * 创建文件夹
     *
     * @param fileName        文件名称
     * @param forceCreateFlag 如果文件存在,是否强制创建新文件
     * @return
     */
    public static void createFolder(String fileName, Boolean forceCreateFlag) {
        File file = new File(fileName);
        try {
            //如果文件目录不存在，自动创建目录
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //如果强制创建新文件,则先删除文件
            if (file.exists() && Boolean.TRUE.equals(forceCreateFlag)) {
                Files.delete(file.toPath());
            }
        } catch (Exception e) {
            log.error("创建文件失败", e);
            throw new HouseException("8888", "创建文件失败：" + e.getMessage());
        }

    }

    /**
     * 创建文件
     *
     * @param fileName        文件名称
     * @param filecontent     文件名称
     * @param forceCreateFlag 如果文件存在,是否强制创建新文件
     * @return
     */
    public static void createFile(String fileName, String filecontent, Boolean forceCreateFlag) {
        log.info("文件："+fileName+"开始创建开始……");
        File file = new File(fileName);
        try {
            //如果文件目录不存在，自动创建目录
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //如果强制创建新文件,则先删除文件
            if (file.exists() && Boolean.TRUE.equals(forceCreateFlag)) {
                log.info("文件:"+fileName+"已存在，删除文件……");
                Files.delete(file.toPath());
            }
            if (!file.exists()|| Boolean.TRUE.equals(forceCreateFlag)) {
                log.info("文件:"+fileName+"不存在，创建文件……");
                if(!file.createNewFile()){
                    throw new HouseException("8888","创建文件失败");
                }
                log.info("success create file,the file is " + fileName);
                //创建文件成功后，写入内容到文件里
                writeFileContent(fileName, filecontent);
            }
        } catch (Exception e) {
            log.error("创建文件失败",e);
            throw new HouseException("8888", "创建文件失败：" + e.getMessage());
        }

    }

    /**
     * 向文件中写入内容
     *
     * @param filepath 文件路径与名称
     * @param newstr   写入的内容
     * @return
     * @throws IOException
     */
    public static void writeFileContent(String filepath, String newstr) throws IOException {
        //新写入的行，换行
        String filein = newstr;
        String temp = "";
        StringBuffer buffer = new StringBuffer();
        try {
            File file = new File(filepath);
            //文件路径(包括文件名称)
           try(
                   //将文件读入输入流
                   FileInputStream fis = new FileInputStream(file);
                   InputStreamReader isr = new InputStreamReader(fis, EncodingEnum.UTF8.getValue());
                   BufferedReader br = new BufferedReader(isr);
                   FileOutputStream fos = new FileOutputStream(file);
                   OutputStreamWriter   oStreamWriter = new OutputStreamWriter(fos, EncodingEnum.UTF8.getValue());
          ){
               //文件原有内容
               for (int i = 0; (temp = br.readLine()) != null; i++) {
                   buffer.append(temp);
                   // 行与行之间的分隔符 相当于“\n”
                   buffer = buffer.append(System.getProperty("line.separator"));
               }
               buffer.append(filein);
               oStreamWriter.append(buffer.toString());
               oStreamWriter.close();
           }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("写入文件内容失败",e);
            throw new HouseException("888","写入文件内容失败",e);
        } finally {
        }
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名称
     * @return
     */
    public static boolean delFile(String fileName) {
        Boolean bool = false;
        File file = new File(fileName);
        try {
            if (file.exists()) {
                Files.delete(file.toPath());
                bool = true;
            }
        } catch (Exception e) {
           log.error("删除文件失败",e);
        }
        return bool;
    }


    /**
     * 读取文件内容
     *
     * @param filePath
     * @param charsetName
     * @return
     */
    public static String readFile(String filePath, String charsetName) throws Exception {
        StringBuffer fileContent = new StringBuffer();
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                try(InputStreamReader isr = new InputStreamReader(new FileInputStream(file), EncodingEnum.UTF8.getValue());
                    BufferedReader br=new BufferedReader(isr);){
                    String lineTxt = "";
                    while ((lineTxt = br.readLine()) != null) {
                        fileContent.append(lineTxt);
                        fileContent.append("\n");
                    }
                }
            } else {
                throw new Exception("文件不存在,文件地址：" + filePath);
            }
        } catch (Exception e) {
            throw new Exception("文件读取错误,文件地址：" + filePath + e.getMessage());
        }finally {

        }
        return fileContent.toString();

    }

    /**
     * 读取文件内容 默认utf-8
     *
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) throws Exception {
        String charsetName =  EncodingEnum.UTF8.getValue();
        return FileUtils.readFile(filePath, charsetName);
    }


    public static void main(String[] args) {
        String filePath="//fileExport//manual//sqoopFile///fileExport//manual//sqoopFile///fileExport//auto//sqoopFile//WIND_HKINDEXEODPRICES_sqoop_import.sh";
        String fileName= filePath.substring(filePath.lastIndexOf("//")+2);
        System.out.println(fileName);
    }

    /**
     * 下载项目根目录下doc下的文件
     *
     * @param response response
     * @param fileName 文件名
     * @return 返回结果 成功或者文件不存在
     */
    public static boolean downloadFile(HttpServletResponse response, String path, String fileName) {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName,  EncodingEnum.UTF8.getValue()));
        } catch (UnsupportedEncodingException e2) {
           log.error("下载文件失败",e2);
        }
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(path + File.separator + fileName)));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, i);
                os.flush();
                i = bis.read(buff);
            }
        } catch (FileNotFoundException e1) {
            log.error("系统找不到指定的文件");
            return false;
        } catch (IOException e) {
            log.error("文件下载出错", e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("文件下载出错", e);
                }
            }
        }
        return true;
    }


    /**
     * 创建ZIP文件
     * @param sourcePath 目标文件或文件夹路径
     * @param filePathAndName 生成的zip文件存在路径（包括文件名）
     */
    public static void createZip(String sourcePath, String filePathAndName) {
        ZipOutputStream zos = null;
        FileOutputStream fos=null;
        try {
            //实际上此压缩包并不存在，只是目标压缩包文件
             fos = new FileOutputStream(filePathAndName);
            zos = new ZipOutputStream(fos);
            //如果是浏览器请求下载zip
            writeZip(new File(sourcePath), "", zos);
        } catch (FileNotFoundException e) {
            log.error("创建ZIP文件失败",e);
        } catch (IOException e) {
            log.error("创建ZIP文件失败",e);
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                log.error("创建ZIP文件失败",e);
            }

        }
    }

    /**
     * 压缩zip,循环压缩子目录文件
     */
    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if(file.exists()){
            if(file.isDirectory()){//处理文件夹
                parentPath+=file.getName()+File.separator;
                File [] files=file.listFiles();
                if(files.length != 0)
                {
                    for(File f:files){
                        writeZip(f, parentPath, zos);
                    }
                }
                else
                {       //空目录则创建当前目录
                    try {
                        zos.putNextEntry(new ZipEntry(parentPath));
                    } catch (IOException e) {
                        log.error("IO异常",e);
                    }
                }
            }else{
                FileInputStream fis=null;
                try {
                    fis=new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());//创建压缩文件
                    zos.putNextEntry(ze);//添加压缩文件
                    byte [] content=new byte[1024];
                    int len;
                    while((len=fis.read(content))!=-1){
                        zos.write(content,0,len);
                        zos.flush();
                    }

                } catch (FileNotFoundException e) {
                    log.error("创建ZIP文件失败",e);
                } catch (IOException e) {
                    log.error("创建ZIP文件失败",e);
                }finally{
                    try {
                        if(fis!=null){
                            fis.close();
                        }
                    }catch(IOException e){
                        log.error("创建ZIP文件失败",e);
                    }
                }
            }
        }
    }

}
