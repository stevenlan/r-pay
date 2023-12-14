package com.rpay.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.util.zip.*;

/**
 * @author steven
 */
public class ZipUtil {
    private static final String ENCODING = "UTF-8";

    /**
     * 压缩加密(zip压缩，base64加密) （先压缩、在base64加密）
     *
     * @param str
     * @param iszip 判断是否base64加密， false表示只做base64操作
     * @return
     */
    public static String zipEncode(String str, boolean iszip) {
        String encodeStr = "";
        try {
            Base64 base64 = new Base64();
            if (iszip) {
                encodeStr = base64.encodeToString(compress(str));
            } else {
                encodeStr = base64.encodeToString(str.getBytes("UTF-8")).trim();
            }
            if (null != encodeStr) {
                encodeStr = encodeStr.replaceAll("\r\n", "").replaceAll("\n", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 压缩
     * <p>
     * 这里需要特别注意的是，如果你想把压缩后的byte[]保存到字符串中， 不能直接使用new
     * String(byte)或者byte.toString()， 因为这样转换之后容量是增加的。 同样的道理，如果是字符串的话，也不能直接使用new
     * String().getBytes()获取byte[]传入到decompress中进行解压缩。 如果保存压缩后的二进制，可以使用new
     * sun.misc.BASE64Encoder().encodeBuffer(byte[] b)将其转换为字符串。 同样解压缩的时候首先使用new
     * BASE64Decoder().decodeBuffer 方法将字符串转换为字节，然后解压就可以了。
     *
     * @param str
     * @return
     */
    private static byte[] compress(String str) {
        if (str == null) {
            return null;
        }

        byte[] compressed;
        ByteArrayOutputStream out = null;
        ZipOutputStream zout = null;

        try {
            out = new ByteArrayOutputStream();
            zout = new ZipOutputStream(out);
            zout.putNextEntry(new ZipEntry("0"));
            zout.write(str.getBytes("UTF-8"));
            zout.closeEntry();
            compressed = out.toByteArray();
        } catch (IOException e) {
            compressed = null;
        } finally {
            if (zout != null) {
                try {
                    zout.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return compressed;
    }

    /**
     * 解密(压缩解密) （先base64解密，在解压）
     *
     * @param zipStr
     * @param iszip  是否是压缩文件， false表示只做base64操作
     * @return
     */
    public static String unzipDecode(String zipStr, boolean iszip) {
        String unzipStr = "";
        Base64 base64 = new Base64();
        try {
            byte[] unzip = base64.decode(zipStr);
            if (iszip) {
                unzipStr = decompress(unzip);
            } else {
                unzipStr = new String(unzip, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return unzipStr;
    }

    /**
     * 解压
     *
     * @param compressed
     * @return
     */
    private static String decompress(byte[] compressed) {
        if (compressed == null) {
            return null;
        }

        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        ZipInputStream zin = null;
        String decompressed;
        try {
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(compressed);
            zin = new ZipInputStream(in);
            ZipEntry entry = zin.getNextEntry();
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = zin.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString("UTF-8");
        } catch (IOException e) {
            decompressed = null;
        } finally {
            if (zin != null) {
                try {
                    zin.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return decompressed;
    }

    public static String encode(String input) {
        try {
            input = new String (Base64.encodeBase64(input.getBytes(ENCODING)),ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            input = "";
        }
        return input;
    }
    public static String encode(byte[] input) {
        String ouput="";
        try {
            ouput = new String (Base64.encodeBase64(input),ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ouput = "";
        }
        return ouput;
    }

    public static String decode(String input) {
        String ouput = "";
        try {
            byte[] decodeBase64 = Base64.decodeBase64(input.getBytes(ENCODING));
            ouput = new String(decodeBase64, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ouput;
    }

    public static byte[] decodeByte(String input) {
        try {
            byte[] decodeBase64 = Base64.decodeBase64(input.getBytes(ENCODING));
            return decodeBase64 ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";
    public static final String GZIP_ENCODE_ISO_8859_1 = "ISO-8859-1";

    public static byte[] compressGZIP(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static byte[] compressGZIP(String str) throws IOException {
        return compressGZIP(str, GZIP_ENCODE_UTF_8);
    }

    public static byte[] uncompressGZIP(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static String uncompressGZIPToString(byte[] bytes, String encoding) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString(encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解压缩加密zip文件
     * @param zip 加密压缩文件
     * @param outFolder 输出的文件夹路径
     * @param pwd 加密密钥
     * @throws IOException IO操作可能发生的异常
     */
//    public void decodeZipForPwd(File zip, File outFolder, String pwd ) throws IOException {
//        if ( !outFolder.exists() ) {
//            outFolder.mkdirs() ;
//        }
//
//        ZipParameters zipParameters = createZipParameters(EncryptionMethod.ZIP_STANDARD, null);
//        zipParameters.setCompressionMethod(CompressionMethod.STORE);
//        net.lingala.zip4j.ZipFile zipFile = new ZipFile(zip, pwd.toCharArray());
//
//        zipFile.extractAll(outFolder.getPath());
//    }

//    private ZipParameters createZipParameters(EncryptionMethod encryptionMethod, AesKeyStrength aesKeyStrength) {
//        ZipParameters zipParameters = new ZipParameters();
//        zipParameters.setEncryptFiles(true);
//        zipParameters.setEncryptionMethod(encryptionMethod);
//        zipParameters.setAesKeyStrength(aesKeyStrength);
//        return zipParameters;
//    }

    public static String uncompressGZIPToString(byte[] bytes) {
        return uncompressGZIPToString(bytes, GZIP_ENCODE_UTF_8);
    }
}
