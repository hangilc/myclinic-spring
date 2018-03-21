package jp.chang.myclinic.shujii;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

class ShujiiDrawer {

    private static Logger logger = LoggerFactory.getLogger(ShujiiDrawer.class);
    private static String coordPropertiesFile = "config/shujii-coord.properties";
    private DrawerCompiler compiler = new DrawerCompiler();
    private Point doctorNamePoint;
    private Point clinicNamePoint;
    private Point clinicAddrPoint;
    private Point clinicPhonePoint;
    private Point clinicFaxPoint;
    private Box detailBox;

    ShujiiDrawer() throws Exception {

        Properties props = new Properties();
        try (InputStream ins = new FileInputStream(coordPropertiesFile)) {
            props.load(ins);
            compiler.createFont("small", "MS Gothic", propNumber(props, "small-font-size"), 0, false);
            compiler.createFont("default", "MS Gothic", propNumber(props, "default-font-size"), 0, false);
            doctorNamePoint = new Point(
                    propNumber(props, "doctor-name.x"),
                    propNumber(props, "doctor-name.y")
            );
            clinicNamePoint = new Point(
                    propNumber(props, "clinic-name.x"),
                    propNumber(props, "clinic-name.y")
            );
            clinicAddrPoint = new Point(
                    propNumber(props, "clinic-addr.x"),
                    propNumber(props, "clinic-addr.y")
            );
            clinicPhonePoint = new Point(
                    propNumber(props, "clinic-phone.x"),
                    propNumber(props, "clinic-phone.y")
            );
            clinicFaxPoint = new Point(
                    propNumber(props, "clinic-fax.x"),
                    propNumber(props, "clinic-fax.y")
            );
            detailBox = new Box(
                    propNumber(props, "detail.left"),
                    propNumber(props, "detail.top"),
                    propNumber(props, "detail.right"),
                    propNumber(props, "detail.bottom")
            );
        }
    }

    private double propNumber(Properties props, String key) {
        try {
            return Double.parseDouble((String) props.get(key));
        } catch (Exception ex) {
            logger.error("Failed to read property: " + key);
            throw ex;
        }
    }

    public List<Op> getOps() {
        return compiler.getOps();
    }

    public void setDoctorName(String value) {
        compiler.setFont("small");
        compiler.textAt(value, doctorNamePoint, HAlign.Left, VAlign.Bottom);
    }

    public void setClinicName(String value) {
        compiler.setFont("small");
        compiler.textAt(value, clinicNamePoint, HAlign.Left, VAlign.Bottom);
    }

    public void setClinicAddr(String value) {
        compiler.setFont("small");
        compiler.textAt(value, clinicAddrPoint, HAlign.Left, VAlign.Bottom);
    }

    public void setClinicPhone(String value) {
        compiler.setFont("small");
        compiler.textAt(value, clinicPhonePoint, HAlign.Left, VAlign.Bottom);
    }

    public void setClinicFax(String value) {
        compiler.setFont("small");
        compiler.textAt(value, clinicFaxPoint, HAlign.Left, VAlign.Bottom);
    }

    public void setDetail(String detail) {
        compiler.setFont("default");
        compiler.paragraph(detail, detailBox, HAlign.Left, VAlign.Top, 0);
    }
}
