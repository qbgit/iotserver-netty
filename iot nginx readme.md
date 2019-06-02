# tcp load balance
stream{<br>
        <br>
        upstream netty_test{<br>
                server 192.168.0.26:9002 weight=1;<br>
                server 192.168.0.26:9003 weight=1;<br>
        }
<br>
        server{
                listen 6665;
                proxy_pass netty_test;
        }
}
<br>
http {
    include       mime.types;
    default_type  application/octet-stream;
}