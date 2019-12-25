package main

import (
	"bytes"
	"crypto/sha1"
	"errors"
	"flag"
	"fmt"
	"io"
	"io/ioutil"
	"log"
	"os"
	"os/exec"
	"path/filepath"
	"sort"
	"strconv"
	"time"
)

var dbHost string
var port int
var repeatPeriod float64
var maxDumps int
var help bool

func init() {
	flag.StringVar(&dbHost, "h", "", "address of database host")
	flag.IntVar(&port, "p", 3306, "port of database host (default: 3306)")
	flag.Float64Var(&repeatPeriod, "r", 10.0, "repeat period in minutes (default: 10)")
	flag.IntVar(&maxDumps, "max", 20, "maximum number of dump files")
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
		"--single-transaction", "--default-character-set=utf8", "--skip-dump-date",
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

func listDumpedFiles() ([]string, error) {
	dumps, err := filepath.Glob("./data/*.sql")
	if err != nil {
		return nil, err
	}
	sort.Sort(sort.Reverse(sort.StringSlice(dumps)))
	return dumps, nil
}

var notEnoughDumpsErrors error = errors.New("Not enough dump files.")

func getTopTwoDumps() (top string, nextTop string, err error) {
	top = ""
	nextTop = ""
	dumps, err := listDumpedFiles()
	if err != nil {
		return
	}
	if len(dumps) >= 2 {
		return dumps[0], dumps[1], nil
	} else {
		err = notEnoughDumpsErrors
		return
	}
}

func sha1Hash(fname string) ([]byte, error) {
	f, err := os.Open(fname)
	if err != nil {
		return nil, err
	}
	defer f.Close()
	h := sha1.New()
	_, err = io.Copy(h, f)
	if err != nil {
		return nil, err
	}
	return h.Sum(nil), nil
}

func removeDuplicate() error {
	cur, prev, err := getTopTwoDumps()
	if err != nil {
		return err
	}
	curhash, err := sha1Hash(cur)
	if err != nil {
		return err
	}
	prevhash, err := sha1Hash(prev)
	if err != nil {
		return err
	}
	fmt.Printf("comparing %s and %s\n", cur, prev)
	fmt.Printf("%x %s\n", curhash, cur)
	fmt.Printf("%x %s\n", prevhash, prev)
	if bytes.Equal(curhash, prevhash) {
		fmt.Printf("Removing duplicate dump: %s\n", prev)
		return os.Remove(prev)
	}
	return nil
}

func removeOldDumps() error {
	dumps, err := listDumpedFiles()
	if err != nil {
		return err
	}
	if len(dumps) > maxDumps {
		for _, f := range dumps[maxDumps:] {
			fmt.Printf("removing old dump: %s\n", f)
			err = os.Remove(f)
			if err != nil {
				fmt.Fprintf(os.Stderr, "%v\n", err)
			}
		}
	}
	return nil
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
		err = removeDuplicate()
		if err != nil {
			fmt.Printf("%v\n", err)
		}
		err = removeOldDumps()
		if err != nil {
			fmt.Printf("%v\n", err)
		}
		log.Println("done")
		time.Sleep(repeat)
	}
}
