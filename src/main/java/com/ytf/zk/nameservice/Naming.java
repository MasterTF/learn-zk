package com.ytf.zk.nameservice;

import com.ytf.zk.constant.ClientBase;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by yutianfang
 * DATE: 17/4/23星期日.
 */
public class Naming {
    private static final int CLIENT_PORT = 2181;
    private static final String ROOT_PATH = "/root";
    private static final String CHILD_PATH = "/root/childPath";
    private static final String CHILD_PATH_2 = "/root/childPath2";
    static ZooKeeper zk = null;
    public static void main(String[] args) throws Exception{
        try {
            zk = new ZooKeeper("localhost:" + CLIENT_PORT, ClientBase.CONNECTION_TIMEOUT, (watchedEvent) ->{
                    System.out.println(watchedEvent.getPath() + "触发了" + watchedEvent.getType() + "事件!" + "data:" + Naming.getData(watchedEvent.getPath()));
            }
            );

            // 创建根目录
            zk.create(ROOT_PATH, ROOT_PATH.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println(zk.getChildren(ROOT_PATH, true));


            // 创建子目录
            zk.create(CHILD_PATH, "childPath".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            // 取出子目录节点
            System.out.println(ROOT_PATH + "子节点: " + zk.getChildren(ROOT_PATH, true));
            System.out.println(CHILD_PATH + "数据: " + new String(zk.getData(CHILD_PATH,true,null)));

            // 修改子目录节点数据
            zk.setData(CHILD_PATH, "modification".getBytes(), -1);

            System.out.println(new String(zk.getData(CHILD_PATH,true,null)));
            zk.setData(CHILD_PATH, "modification2".getBytes(), -1);

            zk.delete(CHILD_PATH,-1);
            // 删除父目录节点
            zk.delete(ROOT_PATH,-1);
            // 关闭连接
            zk.close();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getData(String path){
        if(path == null){
            return null;
        }
        try {
            return new String(zk.getData(path,false,null));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
