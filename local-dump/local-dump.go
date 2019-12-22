package main

import (
	"fmt"
	"os"
	"os/exec"
	"strconv"
	"time"
)

func getTimestamp() string {
	t := time.Now()
	return t.Format("20060102150405")
}

func dump(host string, port int, fname string) {
	user := "root"
	os.Setenv("MYSQL_PWD", os.Getenv("MYCLINIC_DB_ROOT_PASS"))
	cmd := exec.Command("mysqldump", "-h", host, "-P", strconv.Itoa(port), "-u", user, "myclinic",
		"--result-file="+fname)
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	err := cmd.Run()
	if err != nil {
		fmt.Printf("%v", err)
	}
}

func main() {
	stamp := getTimestamp()
	fname := "data/dump-" + stamp + ".sql"
	dump("127.0.0.1", 3306, fname)
}
