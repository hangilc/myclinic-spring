mvn -pl backend-db install
if( $? ){
    mvn -pl backend-sqlite clean package
}