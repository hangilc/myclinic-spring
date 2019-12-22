package main

import (
	"flag"
	//"fmt"
	"io/ioutil"
	"log"
	"os"
	"os/exec"
	"strconv"
	"time"
)

var dbHost string
var port int
var repeatPeriod float64
var help bool

func init() {
	flag.StringVar(&dbHost, "h", "", "address of database host")
	flag.IntVar(&port, "p", 3306, "port of database host (default: 3306)")
	flag.Float64Var(&repeatPeriod, "r", 10.0, "repeat period in minutes (default: 10)")
	flag.BoolVar(&help, "help", false, "prints help")
	flag.Parse()
}

func getTimestamp() string {
	t := time.Now()
	return t.Format("20060102150405")
}

func ensureDataDir() error {
	_, err := os.Stat("./data")
	if os.IsNotExist(err) {
		return os.Mkdir("data", 0755)
	}
	return err
}

func dump(host string, port int, fname string) error {
	user := "root"
	os.Setenv("MYSQL_PWD", os.Getenv("MYCLINIC_DB_ROOT_PASS"))
	tmpfile := fname + ".tmp"
	cmd := exec.Command("mysqldump", "-h", host, "-P", strconv.Itoa(port), "-u", user,
		"--single-transaction", "--default-character-set=utf8",
		"--result-file="+tmpfile, "myclinic")
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	err := cmd.Run()
	if err != nil {
		os.Remove(tmpfile)
		return err
	}
	err = os.Rename(tmpfile, fname)
	return err
}

func dump_dummy(host string, port int, fname string) error {
	return ioutil.WriteFile(fname, []byte("DUMMY DATA"), 0644)
}

func main() {
	if help {
		flag.Usage()
		os.Exit(1)
	}
	err := ensureDataDir()
	if err != nil {
		log.Fatal("Cannot create data directory.")
	}
	if dbHost == "" {
		log.Println("Address of database host is not specified.")
		flag.Usage()
		os.Exit(1)
	}
	repeat := time.Duration(int64(repeatPeriod * float64(time.Minute)))
	for {
		stamp := getTimestamp()
		fname := "data/dump-" + stamp + ".sql"
		log.Printf("start downloading %s\n", fname)
		err = dump(dbHost, port, fname)
		if err != nil {
			log.Printf("%v\n", err)
		}
		log.Println("done")
		time.Sleep(repeat)
	}
}
