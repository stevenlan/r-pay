//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.csource.fastdfs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import org.csource.common.MyException;
import org.csource.fastdfs.ProtoCommon.RecvPackageInfo;
import org.csource.fastdfs.pool.Connection;

public class TrackerClient {
    protected TrackerGroup tracker_group;
    protected byte errno;

    public TrackerClient() {
        this.tracker_group = ClientGlobal.g_tracker_group;
    }

    public TrackerClient(TrackerGroup tracker_group) {
        this.tracker_group = tracker_group;
    }

    public byte getErrorCode() {
        return this.errno;
    }

    public TrackerServer getTrackerServer() throws IOException {
        return this.tracker_group.getTrackerServer();
    }

    public StorageServer getStoreStorage(TrackerServer trackerServer) throws IOException, MyException {
        String groupName = null;
        return this.getStoreStorage(trackerServer, (String)groupName);
    }

    public StorageServer getStoreStorage(TrackerServer trackerServer, String groupName) throws IOException, MyException {
        if (trackerServer == null) {
            trackerServer = this.getTrackerServer();
        }

        Connection connection = trackerServer.getConnection();
        OutputStream out = connection.getOutputStream();

        StorageServer var35;
        try {
            byte cmd;
            byte out_len;
            if (groupName != null && groupName.length() != 0) {
                cmd = 104;
                out_len = 16;
            } else {
                cmd = 101;
                out_len = 0;
            }

            byte[] header = ProtoCommon.packHeader(cmd, (long)out_len, (byte)0);
            out.write(header);
            if (groupName != null && groupName.length() > 0) {
                byte[] bs = groupName.getBytes(ClientGlobal.g_charset);
                byte[] bGroupName = new byte[16];
                int group_len;
                if (bs.length <= 16) {
                    group_len = bs.length;
                } else {
                    group_len = 16;
                }

                Arrays.fill(bGroupName, (byte)0);
                System.arraycopy(bs, 0, bGroupName, 0, group_len);
                out.write(bGroupName);
            }

            RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, ProtoCommon.ExpectBodyLen);
            this.errno = pkgInfo.errno;
            if (pkgInfo.errno != 0) {
                var35 = null;
                return var35;
            }

            String ip_addr = (new String(pkgInfo.body, 16, 15)).trim();
            int port = (int)ProtoCommon.buff2long(pkgInfo.body, 31);
            if ( port == 0 ) {
                port = 23000 ;
            }
            byte store_path = pkgInfo.body[39];
            var35 = new StorageServer(ip_addr, port, store_path);
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
            if (connection != null) {
                try {
                    connection.release();
                } catch (IOException var29) {
                    var29.printStackTrace();
                }
            }

        }

        return var35;
    }

    public StorageServer[] getStoreStorages(TrackerServer trackerServer, String groupName) throws IOException, MyException {
        if (trackerServer == null) {
            trackerServer = this.getTrackerServer();
            if (trackerServer == null) {
                return null;
            }
        }

        Connection connection = trackerServer.getConnection();
        OutputStream out = connection.getOutputStream();

        Object var43;
        try {
            byte cmd;
            byte out_len;
            if (groupName != null && groupName.length() != 0) {
                cmd = 107;
                out_len = 16;
            } else {
                cmd = 106;
                out_len = 0;
            }

            byte[] header = ProtoCommon.packHeader(cmd, (long)out_len, (byte)0);
            out.write(header);
            if (groupName != null && groupName.length() > 0) {
                byte[] bs = groupName.getBytes(ClientGlobal.g_charset);
                byte[] bGroupName = new byte[16];
                int group_len;
                if (bs.length <= 16) {
                    group_len = bs.length;
                } else {
                    group_len = 16;
                }

                Arrays.fill(bGroupName, (byte)0);
                System.arraycopy(bs, 0, bGroupName, 0, group_len);
                out.write(bGroupName);
            }

            RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, -1L);
            this.errno = pkgInfo.errno;
            if (pkgInfo.errno != 0) {
                var43 = null;
                return (StorageServer[])var43;
            }

            if (pkgInfo.body.length >= 40) {
                int ipPortLen = pkgInfo.body.length - 17;

                if (ipPortLen % 23 != 0) {
                    this.errno = 22;
                    Object var46 = null;
                    return (StorageServer[])var46;
                }

                int serverCount = ipPortLen / 23;
                StorageServer[] results;
                if (serverCount > 16) {
                    this.errno = 28;
                    results = null;
                    return results;
                }

                results = new StorageServer[serverCount];
                byte store_path = pkgInfo.body[pkgInfo.body.length - 1];
                int offset = 16;

                for(int i = 0; i < serverCount; ++i) {
                    String ip_addr = (new String(pkgInfo.body, offset, 15)).trim();
                    offset += 15;
                    int port = (int)ProtoCommon.buff2long(pkgInfo.body, offset);
                    if ( port == 0 ) {
                        port = 23000 ;
                    }
                    offset += 8;
                    results[i] = new StorageServer(ip_addr, port, store_path);
                }

                StorageServer[] var47 = results;
                return var47;
            }

            this.errno = 22;
            var43 = null;
        } catch (IOException var40) {
            try {
                connection.close();
            } catch (IOException var38) {
                var38.printStackTrace();
            } finally {
                connection = null;
            }

            throw var40;
        } finally {
            if (connection != null) {
                try {
                    connection.release();
                } catch (IOException var37) {
                    var37.printStackTrace();
                }
            }

        }

        return (StorageServer[])var43;
    }

    public StorageServer getFetchStorage(TrackerServer trackerServer, String groupName, String filename) throws IOException, MyException {
        ServerInfo[] servers = this.getStorages(trackerServer, (byte)102, groupName, filename);
        return servers == null ? null : new StorageServer(servers[0].getIpAddr(), servers[0].getPort(), 0);
    }

    public StorageServer getUpdateStorage(TrackerServer trackerServer, String groupName, String filename) throws IOException, MyException {
        ServerInfo[] servers = this.getStorages(trackerServer, (byte)103, groupName, filename);
        return servers == null ? null : new StorageServer(servers[0].getIpAddr(), servers[0].getPort(), 0);
    }

    public ServerInfo[] getFetchStorages(TrackerServer trackerServer, String groupName, String filename) throws IOException, MyException {
        return this.getStorages(trackerServer, (byte)105, groupName, filename);
    }

    protected ServerInfo[] getStorages(TrackerServer trackerServer, byte cmd, String groupName, String filename) throws IOException, MyException {
        if (trackerServer == null) {
            trackerServer = this.getTrackerServer();
            if (trackerServer == null) {
                return null;
            }
        }

        Connection connection = trackerServer.getConnection();
        OutputStream out = connection.getOutputStream();

        Object var16;
        try {
            byte[] bs = groupName.getBytes(ClientGlobal.g_charset);
            byte[] bGroupName = new byte[16];
            byte[] bFileName = filename.getBytes(ClientGlobal.g_charset);
            int len;
            if (bs.length <= 16) {
                len = bs.length;
            } else {
                len = 16;
            }

            Arrays.fill(bGroupName, (byte)0);
            System.arraycopy(bs, 0, bGroupName, 0, len);
            byte[] header = ProtoCommon.packHeader(cmd, (long)(16 + bFileName.length), (byte)0);
            byte[] wholePkg = new byte[header.length + bGroupName.length + bFileName.length];
            System.arraycopy(header, 0, wholePkg, 0, header.length);
            System.arraycopy(bGroupName, 0, wholePkg, header.length, bGroupName.length);
            System.arraycopy(bFileName, 0, wholePkg, header.length + bGroupName.length, bFileName.length);
            out.write(wholePkg);
            RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, -1L);
            this.errno = pkgInfo.errno;
            if (pkgInfo.errno == 0) {
                if (pkgInfo.body.length < 39) {
                    throw new IOException("Invalid body length: " + pkgInfo.body.length);
                }

                if ((pkgInfo.body.length - 39) % 15 != 0) {
                    throw new IOException("Invalid body length: " + pkgInfo.body.length);
                }

                int server_count = 1 + (pkgInfo.body.length - 39) / 15;
                String ip_addr = (new String(pkgInfo.body, 16, 15)).trim();
                int offset = 31;
                int port = (int)ProtoCommon.buff2long(pkgInfo.body, offset);
                if ( port == 0 ) {
                    port = 23000 ;
                }
                offset = offset + 8;
                ServerInfo[] servers = new ServerInfo[server_count];
                servers[0] = new ServerInfo(ip_addr, port);

                for(int i = 1; i < server_count; ++i) {
                    servers[i] = new ServerInfo((new String(pkgInfo.body, offset, 15)).trim(), port);
                    offset += 15;
                }

                ServerInfo[] var43 = servers;
                return var43;
            }

            var16 = null;
        } catch (IOException var39) {
            try {
                connection.close();
            } catch (IOException var37) {
                var37.printStackTrace();
            } finally {
                connection = null;
            }

            throw var39;
        } finally {
            if (connection != null) {
                try {
                    connection.release();
                } catch (IOException var36) {
                    var36.printStackTrace();
                }
            }

        }

        return (ServerInfo[])var16;
    }

    public StorageServer getFetchStorage1(TrackerServer trackerServer, String file_id) throws IOException, MyException {
        String[] parts = new String[2];
        this.errno = StorageClient1.split_file_id(file_id, parts);
        return this.errno != 0 ? null : this.getFetchStorage(trackerServer, parts[0], parts[1]);
    }

    public ServerInfo[] getFetchStorages1(TrackerServer trackerServer, String file_id) throws IOException, MyException {
        String[] parts = new String[2];
        this.errno = StorageClient1.split_file_id(file_id, parts);
        return this.errno != 0 ? null : this.getFetchStorages(trackerServer, parts[0], parts[1]);
    }

    public StructGroupStat[] listGroups(TrackerServer trackerServer) throws IOException, MyException {
        if (trackerServer == null) {
            trackerServer = this.getTrackerServer();
            if (trackerServer == null) {
                return null;
            }
        }

        Connection connection = trackerServer.getConnection();
        OutputStream out = connection.getOutputStream();

        StructGroupStat[] var12;
        try {
            ProtoStructDecoder decoder;
            try {
                byte[] header = ProtoCommon.packHeader((byte)91, 0L, (byte)0);
                out.write(header);
                RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, -1L);
                this.errno = pkgInfo.errno;
                if (pkgInfo.errno != 0) {
                    decoder = null;
                    return null;
                }

                decoder = new ProtoStructDecoder();
                var12 = (StructGroupStat[])decoder.decode(pkgInfo.body, StructGroupStat.class, StructGroupStat.getFieldsTotalSize());
            } catch (IOException var35) {
                try {
                    connection.close();
                } catch (IOException var33) {
                    var33.printStackTrace();
                } finally {
                    connection = null;
                }

                throw var35;
            } catch (Exception var36) {
                var36.printStackTrace();
                this.errno = 22;
                decoder = null;
                return null;
            }
        } finally {
            if (connection != null) {
                try {
                    connection.release();
                } catch (IOException var32) {
                    var32.printStackTrace();
                }
            }

        }

        return var12;
    }

    public StructStorageStat[] listStorages(TrackerServer trackerServer, String groupName) throws IOException, MyException {
        String storageIpAddr = null;
        return this.listStorages(trackerServer, groupName, (String)storageIpAddr);
    }

    public StructStorageStat[] listStorages(TrackerServer trackerServer, String groupName, String storageIpAddr) throws IOException, MyException {
        if (trackerServer == null) {
            trackerServer = this.getTrackerServer();
            if (trackerServer == null) {
                return null;
            }
        }

        Connection connection = trackerServer.getConnection();
        OutputStream out = connection.getOutputStream();

        Object var11;
        try {
            byte[] bs = groupName.getBytes(ClientGlobal.g_charset);
            byte[] bGroupName = new byte[16];
            int len;
            if (bs.length <= 16) {
                len = bs.length;
            } else {
                len = 16;
            }

            Arrays.fill(bGroupName, (byte)0);
            System.arraycopy(bs, 0, bGroupName, 0, len);
            byte[] bIpAddr;
            int ipAddrLen;
            if (storageIpAddr != null && storageIpAddr.length() > 0) {
                bIpAddr = storageIpAddr.getBytes(ClientGlobal.g_charset);
                if (bIpAddr.length < 16) {
                    ipAddrLen = bIpAddr.length;
                } else {
                    ipAddrLen = 15;
                }
            } else {
                bIpAddr = null;
                ipAddrLen = 0;
            }

            byte[] header = ProtoCommon.packHeader((byte)92, (long)(16 + ipAddrLen), (byte)0);
            byte[] wholePkg = new byte[header.length + bGroupName.length + ipAddrLen];
            System.arraycopy(header, 0, wholePkg, 0, header.length);
            System.arraycopy(bGroupName, 0, wholePkg, header.length, bGroupName.length);
            if (ipAddrLen > 0) {
                System.arraycopy(bIpAddr, 0, wholePkg, header.length + bGroupName.length, ipAddrLen);
            }

            out.write(wholePkg);
            RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, -1L);
            this.errno = pkgInfo.errno;
            ProtoStructDecoder decoder;
            if (pkgInfo.errno == 0) {
                decoder = new ProtoStructDecoder();
                StructStorageStat[] var15 = (StructStorageStat[])decoder.decode(pkgInfo.body, StructStorageStat.class, StructStorageStat.getFieldsTotalSize());
                return var15;
            }

            decoder = null;
            return null;
        } catch (IOException var29) {
            try {
                connection.close();
            } catch (IOException var28) {
                var28.printStackTrace();
            }

            throw var29;
        } catch (Exception var30) {
            var30.printStackTrace();
            this.errno = 22;
            var11 = null;
        } finally {
            if (connection != null) {
                try {
                    connection.release();
                } catch (IOException var27) {
                    var27.printStackTrace();
                }
            }

        }

        return (StructStorageStat[])var11;
    }

    private boolean deleteStorage(TrackerServer trackerServer, String groupName, String storageIpAddr) throws IOException, MyException {
        Connection connection = trackerServer.getConnection();
        OutputStream out = connection.getOutputStream();

        boolean var14;
        try {
            byte[] bs = groupName.getBytes(ClientGlobal.g_charset);
            byte[] bGroupName = new byte[16];
            int len;
            if (bs.length <= 16) {
                len = bs.length;
            } else {
                len = 16;
            }

            Arrays.fill(bGroupName, (byte)0);
            System.arraycopy(bs, 0, bGroupName, 0, len);
            byte[] bIpAddr = storageIpAddr.getBytes(ClientGlobal.g_charset);
            int ipAddrLen;
            if (bIpAddr.length < 16) {
                ipAddrLen = bIpAddr.length;
            } else {
                ipAddrLen = 15;
            }

            byte[] header = ProtoCommon.packHeader((byte)93, (long)(16 + ipAddrLen), (byte)0);
            byte[] wholePkg = new byte[header.length + bGroupName.length + ipAddrLen];
            System.arraycopy(header, 0, wholePkg, 0, header.length);
            System.arraycopy(bGroupName, 0, wholePkg, header.length, bGroupName.length);
            System.arraycopy(bIpAddr, 0, wholePkg, header.length + bGroupName.length, ipAddrLen);
            out.write(wholePkg);
            RecvPackageInfo pkgInfo = ProtoCommon.recvPackage(connection.getInputStream(), (byte)100, 0L);
            this.errno = pkgInfo.errno;
            var14 = pkgInfo.errno == 0;
        } catch (IOException var24) {
            try {
                connection.close();
            } finally {
                connection = null;
            }

            throw var24;
        } finally {
            if (connection != null) {
                connection.release();
            }

        }

        return var14;
    }

    public boolean deleteStorage(String groupName, String storageIpAddr) throws IOException, MyException {
        return this.deleteStorage(ClientGlobal.g_tracker_group, groupName, storageIpAddr);
    }

    public boolean deleteStorage(TrackerGroup trackerGroup, String groupName, String storageIpAddr) throws IOException, MyException {
        int notFoundCount = 0;

        int serverIndex;
        TrackerServer trackerServer;
        for(serverIndex = 0; serverIndex < trackerGroup.tracker_servers.length; ++serverIndex) {
            try {
                trackerServer = trackerGroup.getTrackerServer(serverIndex);
            } catch (IOException var9) {
                var9.printStackTrace(System.err);
                this.errno = 61;
                return false;
            }

            StructStorageStat[] storageStats = this.listStorages(trackerServer, groupName, storageIpAddr);
            if (storageStats == null) {
                if (this.errno != 2) {
                    return false;
                }

                ++notFoundCount;
            } else if (storageStats.length == 0) {
                ++notFoundCount;
            } else if (storageStats[0].getStatus() == 6 || storageStats[0].getStatus() == 7) {
                this.errno = 16;
                return false;
            }
        }

        if (notFoundCount == trackerGroup.tracker_servers.length) {
            this.errno = 2;
            return false;
        } else {
            notFoundCount = 0;

            for(serverIndex = 0; serverIndex < trackerGroup.tracker_servers.length; ++serverIndex) {
                try {
                    trackerServer = trackerGroup.getTrackerServer(serverIndex);
                } catch (IOException var8) {
                    System.err.println("connect to server " + trackerGroup.tracker_servers[serverIndex].getAddress().getHostAddress() + ":" + trackerGroup.tracker_servers[serverIndex].getPort() + " fail");
                    var8.printStackTrace(System.err);
                    this.errno = 61;
                    return false;
                }

                if (!this.deleteStorage(trackerServer, groupName, storageIpAddr) && this.errno != 0) {
                    if (this.errno == 2) {
                        ++notFoundCount;
                    } else if (this.errno != 114) {
                        return false;
                    }
                }
            }

            if (notFoundCount == trackerGroup.tracker_servers.length) {
                this.errno = 2;
                return false;
            } else {
                if (this.errno == 2) {
                    this.errno = 0;
                }

                return this.errno == 0;
            }
        }
    }
}
