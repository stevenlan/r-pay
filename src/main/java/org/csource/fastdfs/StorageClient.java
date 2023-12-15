//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.csource.fastdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import org.csource.common.Base64;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ProtoCommon.RecvHeaderInfo;
import org.csource.fastdfs.ProtoCommon.RecvPackageInfo;
import org.csource.fastdfs.pool.Connection;

public class StorageClient {
    public static final Base64 base64 = new Base64('-', '_', '.', 0);
    protected TrackerServer trackerServer;
    protected StorageServer storageServer;
    protected byte errno;

    public StorageClient() {
        this.trackerServer = null;
        this.storageServer = null;
    }

    public StorageClient(TrackerServer trackerServer) {
        this.trackerServer = trackerServer;
        this.storageServer = null;
    }

    public StorageClient(TrackerServer trackerServer, StorageServer storageServer) {
        this.trackerServer = trackerServer;
        this.storageServer = storageServer;
    }

    public byte getErrorCode() {
        return this.errno;
    }

    public String[] upload_file(String local_filename, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        String group_name = null;
        return this.upload_file((String)group_name, (String)local_filename, file_ext_name, meta_list);
    }

    protected String[] upload_file(String group_name, String local_filename, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        //byte cmd = true;
        return this.upload_file((byte)11, group_name, local_filename, file_ext_name, meta_list);
    }

    protected String[] upload_file(byte cmd, String group_name, String local_filename, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        File f = new File(local_filename);
        FileInputStream fis = new FileInputStream(f);
        if (file_ext_name == null) {
            int nPos = local_filename.lastIndexOf(46);
            if (nPos > 0 && local_filename.length() - nPos <= 7) {
                file_ext_name = local_filename.substring(nPos + 1);
            }
        }

        String[] var12;
        try {
            var12 = this.do_upload_file(cmd, group_name, (String)null, (String)null, file_ext_name, f.length(), new UploadStream(fis, f.length()), meta_list);
        } finally {
            fis.close();
        }

        return var12;
    }

    public String[] upload_file(byte[] file_buff, int offset, int length, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        String group_name = null;
        return this.upload_file((String)group_name, file_buff, offset, length, file_ext_name, meta_list);
    }

    public String[] upload_file(String group_name, byte[] file_buff, int offset, int length, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        return this.do_upload_file((byte)11, group_name, (String)null, (String)null, file_ext_name, (long)length, new StorageClient.UploadBuff(file_buff, offset, length), meta_list);
    }

    public String[] upload_file(byte[] file_buff, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        String group_name = null;
        return this.upload_file((String)group_name, file_buff, 0, file_buff.length, file_ext_name, meta_list);
    }

    public String[] upload_file(String group_name, byte[] file_buff, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        return this.do_upload_file((byte)11, group_name, (String)null, (String)null, file_ext_name, (long)file_buff.length, new StorageClient.UploadBuff(file_buff, 0, file_buff.length), meta_list);
    }

    public String[] upload_file(String group_name, long file_size, UploadCallback callback, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        String master_filename = null;
        String prefix_name = null;
        return this.do_upload_file((byte)11, group_name, (String)master_filename, (String)prefix_name, file_ext_name, file_size, callback, meta_list);
    }

    public String[] upload_file(String group_name, String master_filename, String prefix_name, String local_filename, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        if (group_name != null && group_name.length() != 0 && master_filename != null && master_filename.length() != 0 && prefix_name != null) {
            File f = new File(local_filename);
            FileInputStream fis = new FileInputStream(f);
            if (file_ext_name == null) {
                int nPos = local_filename.lastIndexOf(46);
                if (nPos > 0 && local_filename.length() - nPos <= 7) {
                    file_ext_name = local_filename.substring(nPos + 1);
                }
            }

            String[] var13;
            try {
                var13 = this.do_upload_file((byte)21, group_name, master_filename, prefix_name, file_ext_name, f.length(), new UploadStream(fis, f.length()), meta_list);
            } finally {
                fis.close();
            }

            return var13;
        } else {
            throw new MyException("invalid arguement");
        }
    }

    public String[] upload_file(String group_name, String master_filename, String prefix_name, byte[] file_buff, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        if (group_name != null && group_name.length() != 0 && master_filename != null && master_filename.length() != 0 && prefix_name != null) {
            return this.do_upload_file((byte)21, group_name, master_filename, prefix_name, file_ext_name, (long)file_buff.length, new StorageClient.UploadBuff(file_buff, 0, file_buff.length), meta_list);
        } else {
            throw new MyException("invalid arguement");
        }
    }

    public String[] upload_file(String group_name, String master_filename, String prefix_name, byte[] file_buff, int offset, int length, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        if (group_name != null && group_name.length() != 0 && master_filename != null && master_filename.length() != 0 && prefix_name != null) {
            return this.do_upload_file((byte)21, group_name, master_filename, prefix_name, file_ext_name, (long)length, new StorageClient.UploadBuff(file_buff, offset, length), meta_list);
        } else {
            throw new MyException("invalid arguement");
        }
    }

    public String[] upload_file(String group_name, String master_filename, String prefix_name, long file_size, UploadCallback callback, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        return this.do_upload_file((byte)21, group_name, master_filename, prefix_name, file_ext_name, file_size, callback, meta_list);
    }

    public String[] upload_appender_file(String local_filename, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        String group_name = null;
        return this.upload_appender_file((String)group_name, (String)local_filename, file_ext_name, meta_list);
    }

    protected String[] upload_appender_file(String group_name, String local_filename, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        //byte cmd = true;
        return this.upload_file((byte)23, group_name, local_filename, file_ext_name, meta_list);
    }

    public String[] upload_appender_file(byte[] file_buff, int offset, int length, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        String group_name = null;
        return this.upload_appender_file((String)group_name, file_buff, offset, length, file_ext_name, meta_list);
    }

    public String[] upload_appender_file(String group_name, byte[] file_buff, int offset, int length, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        return this.do_upload_file((byte)23, group_name, (String)null, (String)null, file_ext_name, (long)length, new StorageClient.UploadBuff(file_buff, offset, length), meta_list);
    }

    public String[] upload_appender_file(byte[] file_buff, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        String group_name = null;
        return this.upload_appender_file((String)group_name, file_buff, 0, file_buff.length, file_ext_name, meta_list);
    }

    public String[] upload_appender_file(String group_name, byte[] file_buff, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        return this.do_upload_file((byte)23, group_name, (String)null, (String)null, file_ext_name, (long)file_buff.length, new StorageClient.UploadBuff(file_buff, 0, file_buff.length), meta_list);
    }

    public String[] upload_appender_file(String group_name, long file_size, UploadCallback callback, String file_ext_name, NameValuePair[] meta_list) throws IOException, MyException {
        String master_filename = null;
        String prefix_name = null;
        return this.do_upload_file((byte)23, group_name, (String)master_filename, (String)prefix_name, file_ext_name, file_size, callback, meta_list);
    }

    public int append_file(String group_name, String appender_filename, String local_filename) throws IOException, MyException {
        File f = new File(local_filename);
        FileInputStream fis = new FileInputStream(f);

        int var6;
        try {
            var6 = this.do_append_file(group_name, appender_filename, f.length(), new UploadStream(fis, f.length()));
        } finally {
            fis.close();
        }

        return var6;
    }

    public int append_file(String group_name, String appender_filename, byte[] file_buff) throws IOException, MyException {
        return this.do_append_file(group_name, appender_filename, (long)file_buff.length, new StorageClient.UploadBuff(file_buff, 0, file_buff.length));
    }

    public int append_file(String group_name, String appender_filename, byte[] file_buff, int offset, int length) throws IOException, MyException {
        return this.do_append_file(group_name, appender_filename, (long)length, new StorageClient.UploadBuff(file_buff, offset, length));
    }

    public int append_file(String group_name, String appender_filename, long file_size, UploadCallback callback) throws IOException, MyException {
        return this.do_append_file(group_name, appender_filename, file_size, callback);
    }

    public int modify_file(String group_name, String appender_filename, long file_offset, String local_filename) throws IOException, MyException {
        File f = new File(local_filename);
        FileInputStream fis = new FileInputStream(f);

        int var8;
        try {
            var8 = this.do_modify_file(group_name, appender_filename, file_offset, f.length(), new UploadStream(fis, f.length()));
        } finally {
            fis.close();
        }

        return var8;
    }

    public int modify_file(String group_name, String appender_filename, long file_offset, byte[] file_buff) throws IOException, MyException {
        return this.do_modify_file(group_name, appender_filename, file_offset, (long)file_buff.length, new StorageClient.UploadBuff(file_buff, 0, file_buff.length));
    }

    public int modify_file(String group_name, String appender_filename, long file_offset, byte[] file_buff, int buffer_offset, int buffer_length) throws IOException, MyException {
        return this.do_modify_file(group_name, appender_filename, file_offset, (long)buffer_length, new StorageClient.UploadBuff(file_buff, buffer_offset, buffer_length));
    }

    public int modify_file(String group_name, String appender_filename, long file_offset, long modify_size, UploadCallback callback) throws IOException, MyException {
        return this.do_modify_file(group_name, appender_filename, file_offset, modify_size, callback);
    }

    public String[] regenerate_appender_filename(String group_name, String appender_filename) throws IOException, MyException {
        Connection connection = null;
        if (group_name != null && group_name.length() != 0 && appender_filename != null && appender_filename.length() != 0) {
            boolean bNewStorageServer = this.newUpdatableStorageConnection(group_name, appender_filename);

            String new_group_name;
            try {
                connection = this.storageServer.getConnection();
                byte[] appenderFilenameBytes = appender_filename.getBytes(ClientGlobal.g_charset);
                long body_len = (long)appenderFilenameBytes.length;
                byte[] header = ProtoCommon.packHeader((byte)38, body_len, (byte)0);
                byte[] wholePkg = new byte[(int)((long)header.length + body_len)];
                System.arraycopy(header, 0, wholePkg, 0, header.length);
                int offset = header.length;
                System.arraycopy(appenderFilenameBytes, 0, wholePkg, offset, appenderFilenameBytes.length);
                int var10000 = offset + appenderFilenameBytes.length;
                OutputStream out = connection.getOutputStream();
                out.write(wholePkg);
                RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, -1L);
                this.errno = pkgInfo.errno;
                if (pkgInfo.errno == 0) {
                    if (pkgInfo.body.length <= 16) {
                        throw new MyException("body length: " + pkgInfo.body.length + " <= " + 16);
                    }

                    new_group_name = (new String(pkgInfo.body, 0, 16)).trim();
                    String remote_filename = new String(pkgInfo.body, 16, pkgInfo.body.length - 16);
                    String[] results = new String[]{new_group_name, remote_filename};
                    String[] var17 = results;
                    return var17;
                }

                new_group_name = null;
            } catch (IOException var30) {
                try {
                    connection.close();
                } catch (IOException var28) {
                    var28.printStackTrace();
                } finally {
                    connection = null;
                }

                throw var30;
            } finally {
                this.releaseConnection(connection, bNewStorageServer);
            }

            return new String[]{new_group_name};
        } else {
            this.errno = 22;
            return null;
        }
    }

    protected String[] do_upload_file(byte cmd, String group_name, String master_filename, String prefix_name, String file_ext_name, long file_size, UploadCallback callback, NameValuePair[] meta_list) throws IOException, MyException {
        Connection connection = null;
        boolean bUploadSlave = group_name != null && group_name.length() > 0 && master_filename != null && master_filename.length() > 0 && prefix_name != null;
        boolean bNewStorageServer;
        if (bUploadSlave) {
            bNewStorageServer = this.newUpdatableStorageConnection(group_name, master_filename);
        } else {
            bNewStorageServer = this.newWritableStorageConnection(group_name);
        }

        String[] var28;
        try {
            connection = this.storageServer.getConnection();
            byte[] ext_name_bs = new byte[6];
            Arrays.fill(ext_name_bs, (byte)0);
            if (file_ext_name != null && file_ext_name.length() > 0) {
                byte[] bs = file_ext_name.getBytes(ClientGlobal.g_charset);
                int ext_name_len = bs.length;
                if (ext_name_len > 6) {
                    ext_name_len = 6;
                }

                System.arraycopy(bs, 0, ext_name_bs, 0, ext_name_len);
            }

            byte[] sizeBytes;
            byte[] hexLenBytes;
            byte[] masterFilenameBytes;
            int offset;
            long body_len;
            if (bUploadSlave) {
                masterFilenameBytes = master_filename.getBytes(ClientGlobal.g_charset);
                sizeBytes = new byte[16];
                body_len = (long)(sizeBytes.length + 16 + 6 + masterFilenameBytes.length) + file_size;
                hexLenBytes = ProtoCommon.long2buff((long)master_filename.length());
                System.arraycopy(hexLenBytes, 0, sizeBytes, 0, hexLenBytes.length);
                offset = hexLenBytes.length;
            } else {
                masterFilenameBytes = null;
                sizeBytes = new byte[9];
                body_len = (long)(sizeBytes.length + 6) + file_size;
                sizeBytes[0] = (byte)this.storageServer.getStorePathIndex();
                offset = 1;
            }

            hexLenBytes = ProtoCommon.long2buff(file_size);
            System.arraycopy(hexLenBytes, 0, sizeBytes, offset, hexLenBytes.length);
            OutputStream out = connection.getOutputStream();
            byte[] header = ProtoCommon.packHeader(cmd, body_len, (byte)0);
            byte[] wholePkg = new byte[(int)((long)header.length + body_len - file_size)];
            System.arraycopy(header, 0, wholePkg, 0, header.length);
            System.arraycopy(sizeBytes, 0, wholePkg, header.length, sizeBytes.length);
            offset = header.length + sizeBytes.length;
            int result;
            if (bUploadSlave) {
                byte[] prefix_name_bs = new byte[16];
                byte[] bs = prefix_name.getBytes(ClientGlobal.g_charset);
                result = bs.length;
                Arrays.fill(prefix_name_bs, (byte)0);
                if (result > 16) {
                    result = 16;
                }

                if (result > 0) {
                    System.arraycopy(bs, 0, prefix_name_bs, 0, result);
                }

                System.arraycopy(prefix_name_bs, 0, wholePkg, offset, prefix_name_bs.length);
                offset += prefix_name_bs.length;
            }

            System.arraycopy(ext_name_bs, 0, wholePkg, offset, ext_name_bs.length);
            offset += ext_name_bs.length;
            if (bUploadSlave) {
                System.arraycopy(masterFilenameBytes, 0, wholePkg, offset, masterFilenameBytes.length);
                int var10000 = offset + masterFilenameBytes.length;
            }

            out.write(wholePkg);
            RecvPackageInfo pkgInfo;
            if ((this.errno = (byte)callback.send(out)) != 0) {
                pkgInfo = null;
                return null;
            }

            pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, -1L);
            this.errno = pkgInfo.errno;
            String[] results;
            if (pkgInfo.errno != 0) {
                results = null;
                return results;
            }

            if (pkgInfo.body.length <= 16) {
                throw new MyException("body length: " + pkgInfo.body.length + " <= " + 16);
            }

            String new_group_name = (new String(pkgInfo.body, 0, 16)).trim();
            String remote_filename = new String(pkgInfo.body, 16, pkgInfo.body.length - 16);
            results = new String[]{new_group_name, remote_filename};
            if (meta_list == null || meta_list.length == 0) {
                String[] var62 = results;
                return var62;
            }

            result = 0;
            boolean var51 = false;

            try {
                var51 = true;
                result = this.set_metadata(new_group_name, remote_filename, meta_list, (byte)79);
                var51 = false;
            } catch (IOException var54) {
                result = 5;
                throw var54;
            } finally {
                if (var51) {
                    if (result != 0) {
                        this.errno = (byte)result;
                        this.delete_file(new_group_name, remote_filename);
                        Object var30 = null;
                        return (String[])var30;
                    }
                }
            }

            if (result == 0) {
                var28 = results;
                return var28;
            }

            this.errno = (byte)result;
            this.delete_file(new_group_name, remote_filename);
            var28 = null;
        } catch (IOException var56) {
            try {
                connection.close();
            } catch (IOException var52) {
                var52.printStackTrace();
            } finally {
                connection = null;
            }

            throw var56;
        } finally {
            this.releaseConnection(connection, bNewStorageServer);
        }

        return var28;
    }

    protected int do_append_file(String group_name, String appender_filename, long file_size, UploadCallback callback) throws IOException, MyException {
        Connection connection = null;
        if (group_name != null && group_name.length() != 0 && appender_filename != null && appender_filename.length() != 0) {
            boolean bNewStorageServer = this.newUpdatableStorageConnection(group_name, appender_filename);

            byte var17;
            try {
                connection = this.storageServer.getConnection();
                byte[] appenderFilenameBytes = appender_filename.getBytes(ClientGlobal.g_charset);
                long body_len = (long)(16 + appenderFilenameBytes.length) + file_size;
                byte[] header = ProtoCommon.packHeader((byte)24, body_len, (byte)0);
                byte[] wholePkg = new byte[(int)((long)header.length + body_len - file_size)];
                System.arraycopy(header, 0, wholePkg, 0, header.length);
                int offset = header.length;
                byte[] hexLenBytes = ProtoCommon.long2buff((long)appender_filename.length());
                System.arraycopy(hexLenBytes, 0, wholePkg, offset, hexLenBytes.length);
                offset += hexLenBytes.length;
                hexLenBytes = ProtoCommon.long2buff(file_size);
                System.arraycopy(hexLenBytes, 0, wholePkg, offset, hexLenBytes.length);
                offset += hexLenBytes.length;
                OutputStream out = connection.getOutputStream();
                System.arraycopy(appenderFilenameBytes, 0, wholePkg, offset, appenderFilenameBytes.length);
                int var10000 = offset + appenderFilenameBytes.length;
                out.write(wholePkg);
                if ((this.errno = (byte)callback.send(out)) != 0) {
                    byte var32 = this.errno;
                    return var32;
                }

                RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, 0L);
                this.errno = pkgInfo.errno;
                if (pkgInfo.errno == 0) {
                    byte var33 = 0;
                    return var33;
                }

                var17 = this.errno;
            } catch (IOException var30) {
                try {
                    connection.close();
                } catch (IOException var28) {
                    var28.printStackTrace();
                } finally {
                    connection = null;
                }

                throw var30;
            } finally {
                this.releaseConnection(connection, bNewStorageServer);
            }

            return var17;
        } else {
            this.errno = 22;
            return this.errno;
        }
    }

    private void releaseConnection(Connection connection, boolean bNewStorageServer) {
        try {
            if (connection != null) {
                connection.release();
            }
        } catch (IOException var7) {
            var7.printStackTrace();
        } finally {
            if (bNewStorageServer) {
                this.storageServer = null;
            }

        }

    }

    protected int do_modify_file(String group_name, String appender_filename, long file_offset, long modify_size, UploadCallback callback) throws IOException, MyException {
        Connection connection = null;
        if (group_name != null && group_name.length() != 0 && appender_filename != null && appender_filename.length() != 0) {
            boolean bNewStorageServer = this.newUpdatableStorageConnection(group_name, appender_filename);

            byte var18;
            try {
                connection = this.storageServer.getConnection();
                byte[] appenderFilenameBytes = appender_filename.getBytes(ClientGlobal.g_charset);
                long body_len = (long)(24 + appenderFilenameBytes.length) + modify_size;
                byte[] header = ProtoCommon.packHeader((byte)34, body_len, (byte)0);
                byte[] wholePkg = new byte[(int)((long)header.length + body_len - modify_size)];
                System.arraycopy(header, 0, wholePkg, 0, header.length);
                int offset = header.length;
                byte[] hexLenBytes = ProtoCommon.long2buff((long)appender_filename.length());
                System.arraycopy(hexLenBytes, 0, wholePkg, offset, hexLenBytes.length);
                offset += hexLenBytes.length;
                hexLenBytes = ProtoCommon.long2buff(file_offset);
                System.arraycopy(hexLenBytes, 0, wholePkg, offset, hexLenBytes.length);
                offset += hexLenBytes.length;
                hexLenBytes = ProtoCommon.long2buff(modify_size);
                System.arraycopy(hexLenBytes, 0, wholePkg, offset, hexLenBytes.length);
                offset += hexLenBytes.length;
                OutputStream out = connection.getOutputStream();
                System.arraycopy(appenderFilenameBytes, 0, wholePkg, offset, appenderFilenameBytes.length);
                int var10000 = offset + appenderFilenameBytes.length;
                out.write(wholePkg);
                if ((this.errno = (byte)callback.send(out)) == 0) {
                    RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, 0L);
                    this.errno = pkgInfo.errno;
                    if (pkgInfo.errno != 0) {
                        byte var35 = this.errno;
                        return var35;
                    }

                    byte var19 = 0;
                    return var19;
                }

                var18 = this.errno;
            } catch (IOException var32) {
                try {
                    connection.close();
                } catch (IOException var30) {
                    var30.printStackTrace();
                } finally {
                    connection = null;
                }

                throw var32;
            } finally {
                this.releaseConnection(connection, bNewStorageServer);
            }

            return var18;
        } else {
            this.errno = 22;
            return this.errno;
        }
    }

    public int delete_file(String group_name, String remote_filename) throws IOException, MyException {
        boolean bNewStorageServer = this.newUpdatableStorageConnection(group_name, remote_filename);
        Connection connection = this.storageServer.getConnection();

        byte var6;
        try {
            this.send_package((byte)12, group_name, remote_filename, connection);
            RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, 0L);
            this.errno = pkgInfo.errno;
            var6 = pkgInfo.errno;
        } catch (IOException var19) {
            try {
                connection.close();
            } catch (IOException var17) {
                var17.printStackTrace();
            } finally {
                connection = null;
            }

            throw var19;
        } finally {
            this.releaseConnection(connection, bNewStorageServer);
        }

        return var6;
    }

    public int truncate_file(String group_name, String appender_filename) throws IOException, MyException {
        long truncated_file_size = 0L;
        return this.truncate_file(group_name, appender_filename, 0L);
    }

    public int truncate_file(String group_name, String appender_filename, long truncated_file_size) throws IOException, MyException {
        Connection connection = null;
        if (group_name != null && group_name.length() != 0 && appender_filename != null && appender_filename.length() != 0) {
            boolean bNewStorageServer = this.newUpdatableStorageConnection(group_name, appender_filename);

            byte var15;
            try {
                connection = this.storageServer.getConnection();
                byte[] appenderFilenameBytes = appender_filename.getBytes(ClientGlobal.g_charset);
                int body_len = 16 + appenderFilenameBytes.length;
                byte[] header = ProtoCommon.packHeader((byte)36, (long)body_len, (byte)0);
                byte[] wholePkg = new byte[header.length + body_len];
                System.arraycopy(header, 0, wholePkg, 0, header.length);
                int offset = header.length;
                byte[] hexLenBytes = ProtoCommon.long2buff((long)appender_filename.length());
                System.arraycopy(hexLenBytes, 0, wholePkg, offset, hexLenBytes.length);
                offset += hexLenBytes.length;
                hexLenBytes = ProtoCommon.long2buff(truncated_file_size);
                System.arraycopy(hexLenBytes, 0, wholePkg, offset, hexLenBytes.length);
                offset += hexLenBytes.length;
                OutputStream out = connection.getOutputStream();
                System.arraycopy(appenderFilenameBytes, 0, wholePkg, offset, appenderFilenameBytes.length);
                int var10000 = offset + appenderFilenameBytes.length;
                out.write(wholePkg);
                RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, 0L);
                this.errno = pkgInfo.errno;
                var15 = pkgInfo.errno;
            } catch (IOException var28) {
                try {
                    connection.close();
                } catch (IOException var26) {
                    var26.printStackTrace();
                } finally {
                    connection = null;
                }

                throw var28;
            } finally {
                this.releaseConnection(connection, bNewStorageServer);
            }

            return var15;
        } else {
            this.errno = 22;
            return this.errno;
        }
    }

    public byte[] download_file(String group_name, String remote_filename) throws IOException, MyException {
        long file_offset = 0L;
        long download_bytes = 0L;
        return this.download_file(group_name, remote_filename, 0L, 0L);
    }

    public byte[] download_file(String group_name, String remote_filename, long file_offset, long download_bytes) throws IOException, MyException {
        boolean bNewStorageServer = this.newReadableStorageConnection(group_name, remote_filename);
        Connection connection = this.storageServer.getConnection();

        Object var10;
        try {
            this.send_download_package(group_name, remote_filename, file_offset, download_bytes, connection);
            RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, -1L);
            this.errno = pkgInfo.errno;
            if (pkgInfo.errno == 0) {
                byte[] var25 = pkgInfo.body;
                return var25;
            }

            var10 = null;
        } catch (IOException var23) {
            try {
                connection.close();
            } catch (IOException var21) {
                var21.printStackTrace();
            } finally {
                connection = null;
            }

            throw var23;
        } finally {
            this.releaseConnection(connection, bNewStorageServer);
        }

        return (byte[])var10;
    }

    public int download_file(String group_name, String remote_filename, String local_filename) throws IOException, MyException {
        long file_offset = 0L;
        long download_bytes = 0L;
        return this.download_file(group_name, remote_filename, 0L, 0L, local_filename);
    }

    public int download_file(String group_name, String remote_filename, long file_offset, long download_bytes, String local_filename) throws IOException, MyException {
        boolean bNewStorageServer = this.newReadableStorageConnection(group_name, remote_filename);
        Connection connection = this.storageServer.getConnection();

        byte[] buff;
        try {
            FileOutputStream out = new FileOutputStream(local_filename);

            try {
                this.errno = 0;
                this.send_download_package(group_name, remote_filename, file_offset, download_bytes, connection);
                InputStream in = connection.getInputStream();
                RecvHeaderInfo header = ProtoCommon.recvHeader(in, (byte)100, -1L);
                this.errno = header.errno;
                if (header.errno == 0) {
                    buff = new byte[262144];

                    int bytes;
                    for(long remainBytes = header.body_len; remainBytes > 0L; remainBytes -= (long)bytes) {
                        if ((bytes = in.read(buff, 0, remainBytes > (long)buff.length ? buff.length : (int)remainBytes)) < 0) {
                            throw new IOException("recv package size " + (header.body_len - remainBytes) + " != " + header.body_len);
                        }

                        out.write(buff, 0, bytes);
                    }

                    byte var17 = 0;
                    return var17;
                }

                buff = new byte[]{header.errno};
            } catch (IOException var41) {
                if (this.errno == 0) {
                    this.errno = 5;
                }

                throw var41;
            } finally {
                out.close();
                if (this.errno != 0) {
                    (new File(local_filename)).delete();
                }

            }
        } catch (IOException var43) {
            try {
                connection.close();
            } catch (IOException var39) {
                var39.printStackTrace();
            } finally {
                connection = null;
            }

            throw var43;
        } finally {
            this.releaseConnection(connection, bNewStorageServer);
        }

        return buff[0];
    }

    public int download_file(String group_name, String remote_filename, DownloadCallback callback) throws IOException, MyException {
        long file_offset = 0L;
        long download_bytes = 0L;
        return this.download_file(group_name, remote_filename, 0L, 0L, callback);
    }

    public int download_file(String group_name, String remote_filename, long file_offset, long download_bytes, DownloadCallback callback) throws IOException, MyException {
        boolean bNewStorageServer = this.newReadableStorageConnection(group_name, remote_filename);
        Connection connection = this.storageServer.getConnection();

        try {
            this.send_download_package(group_name, remote_filename, file_offset, download_bytes, connection);
            InputStream in = connection.getInputStream();
            RecvHeaderInfo header = ProtoCommon.recvHeader(in, (byte)100, -1L);
            this.errno = header.errno;
            if (header.errno != 0) {
                byte var32 = header.errno;
                return var32;
            } else {
                byte[] buff = new byte[2048];

                int bytes;
                for(long remainBytes = header.body_len; remainBytes > 0L; remainBytes -= (long)bytes) {
                    if ((bytes = in.read(buff, 0, remainBytes > (long)buff.length ? buff.length : (int)remainBytes)) < 0) {
                        throw new IOException("recv package size " + (header.body_len - remainBytes) + " != " + header.body_len);
                    }

                    int result;
                    if ((result = callback.recv(header.body_len, buff, bytes)) != 0) {
                        this.errno = (byte)result;
                        int var17 = result;
                        return var17;
                    }
                }

                byte var33 = 0;
                return var33;
            }
        } catch (IOException var30) {
            try {
                connection.close();
            } catch (IOException var28) {
                var28.printStackTrace();
            } finally {
                connection = null;
            }

            throw var30;
        } finally {
            this.releaseConnection(connection, bNewStorageServer);
        }
    }

    public NameValuePair[] get_metadata(String group_name, String remote_filename) throws IOException, MyException {
        boolean bNewStorageServer = this.newUpdatableStorageConnection(group_name, remote_filename);
        Connection connection = this.storageServer.getConnection();

        NameValuePair[] var6;
        try {
            this.send_package((byte)15, group_name, remote_filename, connection);
            RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, -1L);
            this.errno = pkgInfo.errno;
            if (pkgInfo.errno == 0) {
                var6 = ProtoCommon.split_metadata(new String(pkgInfo.body, ClientGlobal.g_charset));
                return var6;
            }

            var6 = null;
        } catch (IOException var19) {
            try {
                connection.close();
            } catch (IOException var17) {
                var17.printStackTrace();
            } finally {
                connection = null;
            }

            throw var19;
        } finally {
            this.releaseConnection(connection, bNewStorageServer);
        }

        return var6;
    }

    public int set_metadata(String group_name, String remote_filename, NameValuePair[] meta_list, byte op_flag) throws IOException, MyException {
        boolean bNewStorageServer = this.newUpdatableStorageConnection(group_name, remote_filename);
        Connection connection = this.storageServer.getConnection();

        byte var17;
        try {
            byte[] meta_buff;
            if (meta_list == null) {
                meta_buff = new byte[0];
            } else {
                meta_buff = ProtoCommon.pack_metadata(meta_list).getBytes(ClientGlobal.g_charset);
            }

            byte[] filenameBytes = remote_filename.getBytes(ClientGlobal.g_charset);
            byte[] sizeBytes = new byte[16];
            Arrays.fill(sizeBytes, (byte)0);
            byte[] bs = ProtoCommon.long2buff((long)filenameBytes.length);
            System.arraycopy(bs, 0, sizeBytes, 0, bs.length);
            bs = ProtoCommon.long2buff((long)meta_buff.length);
            System.arraycopy(bs, 0, sizeBytes, 8, bs.length);
            byte[] groupBytes = new byte[16];
            bs = group_name.getBytes(ClientGlobal.g_charset);
            Arrays.fill(groupBytes, (byte)0);
            int groupLen;
            if (bs.length <= groupBytes.length) {
                groupLen = bs.length;
            } else {
                groupLen = groupBytes.length;
            }

            System.arraycopy(bs, 0, groupBytes, 0, groupLen);
            byte[] header = ProtoCommon.packHeader((byte)13, (long)(17 + groupBytes.length + filenameBytes.length + meta_buff.length), (byte)0);
            OutputStream out = connection.getOutputStream();
            byte[] wholePkg = new byte[header.length + sizeBytes.length + 1 + groupBytes.length + filenameBytes.length];
            System.arraycopy(header, 0, wholePkg, 0, header.length);
            System.arraycopy(sizeBytes, 0, wholePkg, header.length, sizeBytes.length);
            wholePkg[header.length + sizeBytes.length] = op_flag;
            System.arraycopy(groupBytes, 0, wholePkg, header.length + sizeBytes.length + 1, groupBytes.length);
            System.arraycopy(filenameBytes, 0, wholePkg, header.length + sizeBytes.length + 1 + groupBytes.length, filenameBytes.length);
            out.write(wholePkg);
            if (meta_buff.length > 0) {
                out.write(meta_buff);
            }

            RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, 0L);
            this.errno = pkgInfo.errno;
            var17 = pkgInfo.errno;
        } catch (IOException var30) {
            try {
                connection.close();
            } catch (IOException var28) {
                var28.printStackTrace();
            } finally {
                connection = null;
            }

            throw var30;
        } finally {
            this.releaseConnection(connection, bNewStorageServer);
        }

        return var17;
    }

    public FileInfo get_file_info(String group_name, String remote_filename) throws IOException, MyException {
        if (remote_filename.length() < 44) {
            this.errno = 22;
            return null;
        } else {
            byte[] buff = base64.decodeAuto(remote_filename.substring(10, 37));
            long file_size = ProtoCommon.buff2long(buff, 8);
            byte file_type;
            if ((file_size & 288230376151711744L) != 0L) {
                file_type = 2;
            } else if ((long)remote_filename.length() <= 60L && ((long)remote_filename.length() <= 44L || (file_size & 576460752303423488L) != 0L)) {
                file_type = 1;
            } else {
                file_type = 4;
            }

            if (file_type != 4 && file_type != 2) {
                int create_timestamp = ProtoCommon.buff2int(buff, 4);
                if (file_size >> 63 != 0L) {
                    file_size &= 4294967295L;
                }

                int crc32 = ProtoCommon.buff2int(buff, 16);
                return new FileInfo(false, file_type, file_size, create_timestamp, crc32, ProtoCommon.getIpAddress(buff, 0));
            } else {
                FileInfo fi = this.query_file_info(group_name, remote_filename);
                if (fi == null) {
                    return null;
                } else {
                    fi.setFileType(file_type);
                    return fi;
                }
            }
        }
    }

    public FileInfo query_file_info(String group_name, String remote_filename) throws IOException, MyException {
        boolean bNewStorageServer = this.newUpdatableStorageConnection(group_name, remote_filename);
        Connection connection = this.storageServer.getConnection();

        Object var13;
        try {
            byte[] filenameBytes = remote_filename.getBytes(ClientGlobal.g_charset);
            byte[] groupBytes = new byte[16];
            byte[] bs = group_name.getBytes(ClientGlobal.g_charset);
            Arrays.fill(groupBytes, (byte)0);
            int groupLen;
            if (bs.length <= groupBytes.length) {
                groupLen = bs.length;
            } else {
                groupLen = groupBytes.length;
            }

            System.arraycopy(bs, 0, groupBytes, 0, groupLen);
            byte[] header = ProtoCommon.packHeader((byte)22, (long)(groupBytes.length + filenameBytes.length), (byte)0);
            OutputStream out = connection.getOutputStream();
            byte[] wholePkg = new byte[header.length + groupBytes.length + filenameBytes.length];
            System.arraycopy(header, 0, wholePkg, 0, header.length);
            System.arraycopy(groupBytes, 0, wholePkg, header.length, groupBytes.length);
            System.arraycopy(filenameBytes, 0, wholePkg, header.length + groupBytes.length, filenameBytes.length);
            out.write(wholePkg);
            RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, 70L);
            this.errno = pkgInfo.errno;
            if (pkgInfo.errno == 0) {
                long file_size = ProtoCommon.buff2long(pkgInfo.body, 0);
                int create_timestamp = (int)ProtoCommon.buff2long(pkgInfo.body, 8);
                int crc32 = (int)ProtoCommon.buff2long(pkgInfo.body, 16);
                String source_ip_addr = (new String(pkgInfo.body, 24, 16)).trim();
                FileInfo var18 = new FileInfo(true, (short)1, file_size, create_timestamp, crc32, source_ip_addr);
                return var18;
            }

            var13 = null;
        } catch (IOException var31) {
            try {
                connection.close();
            } catch (IOException var29) {
                var29.printStackTrace();
            } finally {
                connection = null;
            }

            throw var31;
        } finally {
            this.releaseConnection(connection, bNewStorageServer);
        }

        return (FileInfo)var13;
    }

    protected boolean newWritableStorageConnection(String group_name) throws IOException, MyException {
        if (this.storageServer != null) {
            return false;
        } else {
            TrackerClient tracker = new TrackerClient();
            this.storageServer = tracker.getStoreStorage(this.trackerServer, group_name);
            if (this.storageServer == null) {
                throw new MyException("getStoreStorage fail, errno code: " + tracker.getErrorCode());
            } else {
                return true;
            }
        }
    }

    protected boolean newReadableStorageConnection(String group_name, String remote_filename) throws IOException, MyException {
        if (this.storageServer != null) {
            return false;
        } else {
            TrackerClient tracker = new TrackerClient();
            this.storageServer = tracker.getFetchStorage(this.trackerServer, group_name, remote_filename);
            if (this.storageServer == null) {
                throw new MyException("getStoreStorage fail, errno code: " + tracker.getErrorCode());
            } else {
                return true;
            }
        }
    }

    protected boolean newUpdatableStorageConnection(String group_name, String remote_filename) throws IOException, MyException {
        if (this.storageServer != null) {
            return false;
        } else {
            TrackerClient tracker = new TrackerClient();
            this.storageServer = tracker.getUpdateStorage(this.trackerServer, group_name, remote_filename);
            if (this.storageServer == null) {
                throw new MyException("getStoreStorage fail, errno code: " + tracker.getErrorCode());
            } else {
                return true;
            }
        }
    }

    protected void send_package(byte cmd, String group_name, String remote_filename, Connection connection) throws IOException {
        byte[] groupBytes = new byte[16];
        byte[] bs = group_name.getBytes(ClientGlobal.g_charset);
        byte[] filenameBytes = remote_filename.getBytes(ClientGlobal.g_charset);
        Arrays.fill(groupBytes, (byte)0);
        int groupLen;
        if (bs.length <= groupBytes.length) {
            groupLen = bs.length;
        } else {
            groupLen = groupBytes.length;
        }

        System.arraycopy(bs, 0, groupBytes, 0, groupLen);
        byte[] header = ProtoCommon.packHeader(cmd, (long)(groupBytes.length + filenameBytes.length), (byte)0);
        byte[] wholePkg = new byte[header.length + groupBytes.length + filenameBytes.length];
        System.arraycopy(header, 0, wholePkg, 0, header.length);
        System.arraycopy(groupBytes, 0, wholePkg, header.length, groupBytes.length);
        System.arraycopy(filenameBytes, 0, wholePkg, header.length + groupBytes.length, filenameBytes.length);
        connection.getOutputStream().write(wholePkg);
    }

    protected void send_download_package(String group_name, String remote_filename, long file_offset, long download_bytes, Connection connection) throws IOException {
        byte[] bsOffset = ProtoCommon.long2buff(file_offset);
        byte[] bsDownBytes = ProtoCommon.long2buff(download_bytes);
        byte[] groupBytes = new byte[16];
        byte[] bs = group_name.getBytes(ClientGlobal.g_charset);
        byte[] filenameBytes = remote_filename.getBytes(ClientGlobal.g_charset);
        Arrays.fill(groupBytes, (byte)0);
        int groupLen;
        if (bs.length <= groupBytes.length) {
            groupLen = bs.length;
        } else {
            groupLen = groupBytes.length;
        }

        System.arraycopy(bs, 0, groupBytes, 0, groupLen);
        byte[] header = ProtoCommon.packHeader((byte)14, (long)(bsOffset.length + bsDownBytes.length + groupBytes.length + filenameBytes.length), (byte)0);
        byte[] wholePkg = new byte[header.length + bsOffset.length + bsDownBytes.length + groupBytes.length + filenameBytes.length];
        System.arraycopy(header, 0, wholePkg, 0, header.length);
        System.arraycopy(bsOffset, 0, wholePkg, header.length, bsOffset.length);
        System.arraycopy(bsDownBytes, 0, wholePkg, header.length + bsOffset.length, bsDownBytes.length);
        System.arraycopy(groupBytes, 0, wholePkg, header.length + bsOffset.length + bsDownBytes.length, groupBytes.length);
        System.arraycopy(filenameBytes, 0, wholePkg, header.length + bsOffset.length + bsDownBytes.length + groupBytes.length, filenameBytes.length);
        connection.getOutputStream().write(wholePkg);
    }

    public boolean isConnected() {
        return this.trackerServer != null;
    }

    public boolean isAvaliable() {
        return this.trackerServer != null;
    }

    public void close() throws IOException {
        this.trackerServer = null;
    }

    public TrackerServer getTrackerServer() {
        return this.trackerServer;
    }

    public void setTrackerServer(TrackerServer trackerServer) {
        this.trackerServer = trackerServer;
    }

    public StorageServer getStorageServer() {
        return this.storageServer;
    }

    public void setStorageServer(StorageServer storageServer) {
        this.storageServer = storageServer;
    }

    public static class UploadBuff implements UploadCallback {
        private byte[] fileBuff;
        private int offset;
        private int length;

        public UploadBuff(byte[] fileBuff, int offset, int length) {
            this.fileBuff = fileBuff;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public int send(OutputStream out) throws IOException {
            out.write(this.fileBuff, this.offset, this.length);
            return 0;
        }
    }
}
