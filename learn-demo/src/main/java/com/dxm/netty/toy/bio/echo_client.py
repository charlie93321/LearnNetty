import socket
import sys

line_seperator_dic = {
    'linux': '\n'.encode("utf-8"),
    'win32': '\r\n'.encode("utf-8")
}


def line_seperator():
    return line_seperator_dic[sys.platform]


def start_client():
    print(line_seperator_dic)
    ip_port = ('localhost', 5500)

    sk = socket.socket()

    sk.connect(ip_port)

    while True:
        str = input("Enter your input: ")
        if "quit" == str:
            sk.send(str.encode("utf-8"))
            sk.send(line_seperator())
            print("客户端退出......")
            break
        sk.send(str.encode("utf-8"))
        sk.send(line_seperator())
        server_reply = sk.recv(1024)
        print("客户端接收到的数据:{}".format(server_reply.decode(encoding='utf-8')))

    sk.close()


if __name__ == "__main__":
    start_client()
