package me.mynqme.blocks.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import me.mynqme.blocks.Blocks;
import org.bukkit.Bukkit;

public class Database {
    private static Database inst;
    private final ConnectionPoolManager pool;
    private final Blocks plugin;
    public Database(Blocks instance) {
        this.plugin = instance;
        inst = this;
        pool = new ConnectionPoolManager(plugin);
    }

    public void saveBlocks(UUID uuid, long amount) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT * FROM `blocks_broken` WHERE `uuid` = ?");
            ps.setString(1,uuid.toString());
            rs = ps.executeQuery();
            boolean b = rs.next();
            pool.close(conn, ps, rs);
            conn = pool.getConnection();
            ps = conn.prepareStatement(b ? "UPDATE `blocks_broken` SET `blocks` = ? WHERE `uuid` = ?" : "INSERT INTO `blocks_broken` (`blocks`,`uuid`) VALUES (?,?)");
            if (amount < 0) amount = 0;
            ps.setString(1, b ? String.valueOf(amount) : "0");
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }
    }

    public void setBlocks(UUID uuid, int amount) {
        Bukkit.getLogger().info("Saving blocks for " + uuid + "... (" + amount + ")");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("UPDATE `blocks_broken` SET `blocks` = ? WHERE `uuid` = ?");
            if (amount < 0) amount = 0;
            ps.setString(1, String.valueOf(amount));
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, null);
        }
    }

    public int getBlocks(UUID uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("SELECT * FROM `blocks_broken` WHERE `uuid` = ?");
            ps.setString(1,uuid.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                int blocks = rs.getInt("blocks");
                pool.close(conn, ps, rs);
                return blocks;
            } else {
                pool.close(conn, ps, rs);
                conn = pool.getConnection();
                ps = conn.prepareStatement("INSERT INTO `blocks_broken` (`blocks`,`uuid`) VALUES (?,?)");
                ps.setString(1, "0");
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, rs);
        }
        return 0;
    }

    public void createBlocksTable() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = pool.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `blocks_broken` (`uuid` VARCHAR(36) NOT NULL, `blocks` TEXT NOT NULL, PRIMARY KEY (`uuid`));");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.close(conn, ps, null);
        }
    }

    public void closeConnections() {
        pool.closePool();
    }

    public static Database inst() {
        return inst;
    }
}

