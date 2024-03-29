//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.csource.fastdfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;

public class ProtoCommon {
    public static final byte FDFS_PROTO_CMD_QUIT = 82;
    public static final byte TRACKER_PROTO_CMD_SERVER_LIST_GROUP = 91;
    public static final byte TRACKER_PROTO_CMD_SERVER_LIST_STORAGE = 92;
    public static final byte TRACKER_PROTO_CMD_SERVER_DELETE_STORAGE = 93;
    public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE = 101;
    public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ONE = 102;
    public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE = 103;
    public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE = 104;
    public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ALL = 105;
    public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ALL = 106;
    public static final byte TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ALL = 107;
    public static final byte TRACKER_PROTO_CMD_RESP = 100;
    public static final byte FDFS_PROTO_CMD_ACTIVE_TEST = 111;
    public static final byte STORAGE_PROTO_CMD_UPLOAD_FILE = 11;
    public static final byte STORAGE_PROTO_CMD_DELETE_FILE = 12;
    public static final byte STORAGE_PROTO_CMD_SET_METADATA = 13;
    public static final byte STORAGE_PROTO_CMD_DOWNLOAD_FILE = 14;
    public static final byte STORAGE_PROTO_CMD_GET_METADATA = 15;
    public static final byte STORAGE_PROTO_CMD_UPLOAD_SLAVE_FILE = 21;
    public static final byte STORAGE_PROTO_CMD_QUERY_FILE_INFO = 22;
    public static final byte STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE = 23;
    public static final byte STORAGE_PROTO_CMD_APPEND_FILE = 24;
    public static final byte STORAGE_PROTO_CMD_MODIFY_FILE = 34;
    public static final byte STORAGE_PROTO_CMD_TRUNCATE_FILE = 36;
    public static final byte STORAGE_PROTO_CMD_REGENERATE_APPENDER_FILENAME = 38;
    public static final byte STORAGE_PROTO_CMD_RESP = 100;
    public static final byte FDFS_STORAGE_STATUS_INIT = 0;
    public static final byte FDFS_STORAGE_STATUS_WAIT_SYNC = 1;
    public static final byte FDFS_STORAGE_STATUS_SYNCING = 2;
    public static final byte FDFS_STORAGE_STATUS_IP_CHANGED = 3;
    public static final byte FDFS_STORAGE_STATUS_DELETED = 4;
    public static final byte FDFS_STORAGE_STATUS_OFFLINE = 5;
    public static final byte FDFS_STORAGE_STATUS_ONLINE = 6;
    public static final byte FDFS_STORAGE_STATUS_ACTIVE = 7;
    public static final byte FDFS_STORAGE_STATUS_NONE = 99;
    public static final byte STORAGE_SET_METADATA_FLAG_OVERWRITE = 79;
    public static final byte STORAGE_SET_METADATA_FLAG_MERGE = 77;
    public static final int FDFS_PROTO_PKG_LEN_SIZE = 8;
    public static final int FDFS_PROTO_CMD_SIZE = 1;
    public static final int FDFS_GROUP_NAME_MAX_LEN = 16;
    public static final int FDFS_IPADDR_SIZE = 16;
    public static final int FDFS_DOMAIN_NAME_MAX_SIZE = 128;
    public static final int FDFS_VERSION_SIZE = 6;
    public static final int FDFS_STORAGE_ID_MAX_SIZE = 16;
    public static final String FDFS_RECORD_SEPERATOR = "\u0001";
    public static final String FDFS_FIELD_SEPERATOR = "\u0002";
    public static final int TRACKER_QUERY_STORAGE_FETCH_BODY_LEN = 39;
    public static final int TRACKER_QUERY_STORAGE_STORE_BODY_LEN = 40;
    public static final byte FDFS_FILE_EXT_NAME_MAX_LEN = 6;
    public static final byte FDFS_FILE_PREFIX_MAX_LEN = 16;
    public static final byte FDFS_FILE_PATH_LEN = 10;
    public static final byte FDFS_FILENAME_BASE64_LENGTH = 27;
    public static final byte FDFS_TRUNK_FILE_INFO_LEN = 16;
    public static final byte ERR_NO_ENOENT = 2;
    public static final byte ERR_NO_EIO = 5;
    public static final byte ERR_NO_EBUSY = 16;
    public static final byte ERR_NO_EINVAL = 22;
    public static final byte ERR_NO_ENOSPC = 28;
    public static final byte ECONNREFUSED = 61;
    public static final byte ERR_NO_EALREADY = 114;
    public static final long INFINITE_FILE_SIZE = 288230376151711744L;
    public static final long APPENDER_FILE_SIZE = 288230376151711744L;
    public static final long TRUNK_FILE_MARK_SIZE = 576460752303423488L;
    public static final long NORMAL_LOGIC_FILENAME_LENGTH = 44L;
    public static final long TRUNK_LOGIC_FILENAME_LENGTH = 60L;
    public static long ExpectBodyLen ;
    protected static final int PROTO_HEADER_CMD_INDEX = 8;
    protected static final int PROTO_HEADER_STATUS_INDEX = 9;

    private ProtoCommon() {
    }

    public static String getStorageStatusCaption(byte status) {
        switch(status) {
            case 0:
                return "INIT";
            case 1:
                return "WAIT_SYNC";
            case 2:
                return "SYNCING";
            case 3:
                return "IP_CHANGED";
            case 4:
                return "DELETED";
            case 5:
                return "OFFLINE";
            case 6:
                return "ONLINE";
            case 7:
                return "ACTIVE";
            case 99:
                return "NONE";
            default:
                return "UNKOWN";
        }
    }

    public static byte[] packHeader(byte cmd, long pkg_len, byte errno) throws UnsupportedEncodingException {
        byte[] header = new byte[10];
        Arrays.fill(header, (byte)0);
        byte[] hex_len = long2buff(pkg_len);
        System.arraycopy(hex_len, 0, header, 0, hex_len.length);
        header[8] = cmd;
        header[9] = errno;
        return header;
    }

    public static ProtoCommon.RecvHeaderInfo recvHeader(InputStream in, byte expect_cmd, long expect_body_len) throws IOException {
        byte[] header = new byte[10];
        int bytes;
        if ((bytes = in.read(header)) != header.length) {
            throw new IOException("recv package size " + bytes + " != " + header.length);
        } else if (header[8] != expect_cmd) {
            throw new IOException("recv cmd: " + header[8] + " is not correct, expect cmd: " + expect_cmd);
        } else if (header[9] != 0) {
            return new ProtoCommon.RecvHeaderInfo(header[9], 0L);
        } else {
            long pkg_len = buff2long(header, 0);
            if (pkg_len < 0L) {
                throw new IOException("recv body length: " + pkg_len + " < 0!");
            } else if (expect_body_len >= 0L && pkg_len != expect_body_len) {
                throw new IOException("recv body length: " + pkg_len + " is not correct, expect length: " + expect_body_len);
            } else {
                return new ProtoCommon.RecvHeaderInfo((byte)0, pkg_len);
            }
        }
    }

    public static ProtoCommon.RecvPackageInfo recvPackage(InputStream in, byte expect_cmd, long expect_body_len) throws IOException {
        ProtoCommon.RecvHeaderInfo header = recvHeader(in, expect_cmd, expect_body_len);
        if (header.errno != 0) {
            return new ProtoCommon.RecvPackageInfo(header.errno, (byte[])null);
        } else {
            byte[] body = new byte[(int)header.body_len];
            int totalBytes = 0;

            int bytes;
            for(int remainBytes = (int)header.body_len; (long)totalBytes < header.body_len && (bytes = in.read(body, totalBytes, remainBytes)) >= 0; remainBytes -= bytes) {
                totalBytes += bytes;
            }

            if ((long)totalBytes != header.body_len) {
                throw new IOException("recv package size " + totalBytes + " != " + header.body_len);
            } else {
                return new ProtoCommon.RecvPackageInfo((byte)0, body);
            }
        }
    }

    public static NameValuePair[] split_metadata(String meta_buff) {
        return split_metadata(meta_buff, "\u0001", "\u0002");
    }

    public static NameValuePair[] split_metadata(String meta_buff, String recordSeperator, String filedSeperator) {
        String[] rows = meta_buff.split(recordSeperator);
        NameValuePair[] meta_list = new NameValuePair[rows.length];

        for(int i = 0; i < rows.length; ++i) {
            String[] cols = rows[i].split(filedSeperator, 2);
            meta_list[i] = new NameValuePair(cols[0]);
            if (cols.length == 2) {
                meta_list[i].setValue(cols[1]);
            }
        }

        return meta_list;
    }

    public static String pack_metadata(NameValuePair[] meta_list) {
        if (meta_list.length == 0) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer(32 * meta_list.length);
            sb.append(meta_list[0].getName()).append("\u0002").append(meta_list[0].getValue());

            for(int i = 1; i < meta_list.length; ++i) {
                sb.append("\u0001");
                sb.append(meta_list[i].getName()).append("\u0002").append(meta_list[i].getValue());
            }

            return sb.toString();
        }
    }

    public static void closeSocket(Socket sock) throws IOException {
        byte[] header = packHeader((byte)82, 0L, (byte)0);
        sock.getOutputStream().write(header);
        sock.close();
    }

    public static boolean activeTest(Socket sock) throws IOException {
        byte[] header = packHeader((byte)111, 0L, (byte)0);
        sock.getOutputStream().write(header);
        ProtoCommon.RecvHeaderInfo headerInfo = recvHeader(sock.getInputStream(), (byte)100, 0L);
        return headerInfo.errno == 0;
    }

    public static byte[] long2buff(long n) {
        byte[] bs = new byte[]{(byte)((int)(n >> 56 & 255L)), (byte)((int)(n >> 48 & 255L)), (byte)((int)(n >> 40 & 255L)), (byte)((int)(n >> 32 & 255L)), (byte)((int)(n >> 24 & 255L)), (byte)((int)(n >> 16 & 255L)), (byte)((int)(n >> 8 & 255L)), (byte)((int)(n & 255L))};
        return bs;
    }

    public static long buff2long(byte[] bs, int offset) {
        return (long)(bs[offset] >= 0 ? bs[offset] : 256 + bs[offset]) << 56 | (long)(bs[offset + 1] >= 0 ? bs[offset + 1] : 256 + bs[offset + 1]) << 48 | (long)(bs[offset + 2] >= 0 ? bs[offset + 2] : 256 + bs[offset + 2]) << 40 | (long)(bs[offset + 3] >= 0 ? bs[offset + 3] : 256 + bs[offset + 3]) << 32 | (long)(bs[offset + 4] >= 0 ? bs[offset + 4] : 256 + bs[offset + 4]) << 24 | (long)(bs[offset + 5] >= 0 ? bs[offset + 5] : 256 + bs[offset + 5]) << 16 | (long)(bs[offset + 6] >= 0 ? bs[offset + 6] : 256 + bs[offset + 6]) << 8 | (long)(bs[offset + 7] >= 0 ? bs[offset + 7] : 256 + bs[offset + 7]);
    }

    public static int buff2int(byte[] bs, int offset) {
        return (bs[offset] >= 0 ? bs[offset] : 256 + bs[offset]) << 24 | (bs[offset + 1] >= 0 ? bs[offset + 1] : 256 + bs[offset + 1]) << 16 | (bs[offset + 2] >= 0 ? bs[offset + 2] : 256 + bs[offset + 2]) << 8 | (bs[offset + 3] >= 0 ? bs[offset + 3] : 256 + bs[offset + 3]);
    }

    public static String getIpAddress(byte[] bs, int offset) {
        if (bs[0] != 0 && bs[3] != 0) {
            StringBuilder sbResult = new StringBuilder(16);

            for(int i = offset; i < offset + 4; ++i) {
                int n = bs[i] >= 0 ? bs[i] : 256 + bs[i];
                if (sbResult.length() > 0) {
                    sbResult.append(".");
                }

                sbResult.append(String.valueOf(n));
            }

            return sbResult.toString();
        } else {
            return "";
        }
    }

    public static String md5(byte[] source) throws NoSuchAlgorithmException {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(source);
        byte[] tmp = md.digest();
        char[] str = new char[32];
        int k = 0;

        for(int i = 0; i < 16; ++i) {
            str[k++] = hexDigits[tmp[i] >>> 4 & 15];
            str[k++] = hexDigits[tmp[i] & 15];
        }

        return new String(str);
    }

    public static String getToken(String remote_filename, int ts, String secret_key) throws UnsupportedEncodingException, NoSuchAlgorithmException, MyException {
        byte[] bsFilename = remote_filename.getBytes(ClientGlobal.g_charset);
        byte[] bsKey = secret_key.getBytes(ClientGlobal.g_charset);
        byte[] bsTimestamp = (new Integer(ts)).toString().getBytes(ClientGlobal.g_charset);
        byte[] buff = new byte[bsFilename.length + bsKey.length + bsTimestamp.length];
        System.arraycopy(bsFilename, 0, buff, 0, bsFilename.length);
        System.arraycopy(bsKey, 0, buff, bsFilename.length, bsKey.length);
        System.arraycopy(bsTimestamp, 0, buff, bsFilename.length + bsKey.length, bsTimestamp.length);
        return md5(buff);
    }

    public static String genSlaveFilename(String master_filename, String prefix_name, String ext_name) throws MyException {
        if (master_filename.length() < 34) {
            throw new MyException("master filename \"" + master_filename + "\" is invalid");
        } else {
            int dotIndex = master_filename.indexOf(46, master_filename.length() - 7);
            String true_ext_name;
            if (ext_name != null) {
                if (ext_name.length() == 0) {
                    true_ext_name = "";
                } else if (ext_name.charAt(0) == '.') {
                    true_ext_name = ext_name;
                } else {
                    true_ext_name = "." + ext_name;
                }
            } else if (dotIndex < 0) {
                true_ext_name = "";
            } else {
                true_ext_name = master_filename.substring(dotIndex);
            }

            if (true_ext_name.length() == 0 && prefix_name.equals("-m")) {
                throw new MyException("prefix_name \"" + prefix_name + "\" is invalid");
            } else {
                return dotIndex < 0 ? master_filename + prefix_name + true_ext_name : master_filename.substring(0, dotIndex) + prefix_name + true_ext_name;
            }
        }
    }

    public static class RecvHeaderInfo {
        public byte errno;
        public long body_len;

        public RecvHeaderInfo(byte errno, long body_len) {
            this.errno = errno;
            this.body_len = body_len;
        }
    }

    public static class RecvPackageInfo {
        public byte errno;
        public byte[] body;

        public RecvPackageInfo(byte errno, byte[] body) {
            this.errno = errno;
            this.body = body;
        }
    }
}
