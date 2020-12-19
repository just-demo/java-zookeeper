1. Download https://www2.apache.paket.ua/zookeeper/current/
2. Unpack
3. Rename conf\zoo_sample.cfg -> conf\zoo.cfg
4. Run bin\zkServer.cmd
5. Run bin\zkCli.cmd -server 127.0.0.1:2181
6. [zk: 127.0.0.1:2181(CONNECTED) 0] create /DemoKey DemoValue
7. [zk: 127.0.0.1:2181(CONNECTED) 1] get /DemoKey
8. Run java demo