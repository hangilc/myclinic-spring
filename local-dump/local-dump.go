package main

import (
	"log"
	"os"
	"os/exec"
)

func main() {
	user := "root"
	os.Setenv("MYSQL_PWD", os.Getenv("MYCLINIC_DB_ROOT_PASS"))
	cmd := exec.Command("mysqldump", "-h", "127.0.0.1", "-u", user, "myclinic",
		"--result-file=data/dump.sql")
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	err := cmd.Run()
	if err != nil {
		log.Printf("%v", err)
	}
}
