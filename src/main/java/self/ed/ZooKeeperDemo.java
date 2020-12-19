package self.ed;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZKUtil;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static java.lang.System.currentTimeMillis;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.zookeeper.CreateMode.PERSISTENT;
import static org.apache.zookeeper.Watcher.Event.KeeperState.SyncConnected;
import static org.apache.zookeeper.ZooDefs.Ids.OPEN_ACL_UNSAFE;

public class ZooKeeperDemo {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zk = connect("localhost");
        String path = "/DemoKey" + currentTimeMillis();

        System.out.println("Using path: " + path);

        System.out.println("Creating");
        zk.create(path, "DemoValue".getBytes(UTF_8), OPEN_ACL_UNSAFE, PERSISTENT);
        System.out.println("Created: " + new String(zk.getData(path, null, null), UTF_8));

        System.out.println("Updating");
        zk.setData(path, "DemoValueUpdated".getBytes(UTF_8), zk.exists(path, true).getVersion());
        System.out.println("Updated: " + new String(zk.getData(path, null, null), UTF_8));

        System.out.println("Deleting");
        zk.delete(path, zk.exists(path, true).getVersion());
        System.out.println("Deleted: " + zk.exists(path, true));

        System.out.println("Creating children");
        zk.create(path, null, OPEN_ACL_UNSAFE, PERSISTENT);
        zk.create(path + "/DemoChild1", "DemoValue1".getBytes(UTF_8), OPEN_ACL_UNSAFE, PERSISTENT);
        zk.create(path + "/DemoChild2", "DemoValue2".getBytes(UTF_8), OPEN_ACL_UNSAFE, PERSISTENT);
        System.out.println("All keys: " + zk.getChildren("/", null));
        System.out.println("Children keys: " + zk.getChildren(path, null));

        System.out.println("Deleting recursive");
        ZKUtil.deleteRecursive(zk, path);
        System.out.println("Deleted recursive: " + zk.exists(path, true));
        zk.close();
    }

    private static ZooKeeper connect(String host)
            throws IOException,
            InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper(host, 2000, event -> {
            if (event.getState() == SyncConnected) {
                latch.countDown();
            }
        });

        latch.await();
        return zk;
    }
}