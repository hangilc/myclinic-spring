using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MasterManager
{
    class Row
    {
        string[] items;

        public Row(string line)
        {
            items = line.Split(',');
            for (int i = 0; i < items.Length; i++)
            {
                string s = items[i];
                int len = s.Length;
                int start = 0;
                if (s.StartsWith("\""))
                {
                    start += 1;
                    len -= 1;
                }
                if (s.EndsWith("\""))
                {
                    len -= 1;
                }
                items[i] = s.Substring(start, len);
            }
        }

        public int Count
        {
            get { return items.Length; }
        }

        public string GetString(int i)
        {
            return items[i - 1];
        }

        public int GetInt(int i)
        {
            return Int32.Parse(GetString(i));
        }
    }

    /*
	    henkou_kubun = get_csv_value(sa, 1);
	    if( *henkou_kubun == '1'  // 抹消  
                ||
		    *henkou_kubun == '9'  // 廃止 
           )
		    return 1;
	    master_shubetsu = get_csv_value(sa, 2);
	    if( *master_shubetsu != 'Y' ){
		    MessageBox(NULL, "医薬品マスターのデータではありません", "エラー", MB_ICONERROR);
		    return 0;
	    }
	    iyakuhincode = get_csv_value(sa, 3);
	    name = get_csv_value(sa, 5);
	    yomi = get_csv_value(sa, 7);
	    unit = get_csv_value(sa, 10);
	    kingaku_shubetsu = get_csv_value(sa, 11);
	    if( *kingaku_shubetsu != '1' )
		    return 1;
	    yakka = get_csv_value(sa, 12);
	    madoku = get_csv_value(sa, 14);
		    // madoku: 麻薬・毒薬など
		    //   0:右記以外、1:麻薬、2:毒薬、3:覚せい剤原料、5:向精神薬
	    kouhatsu = get_csv_value(sa, 17);
		    // kouhatsu: 後発品
		    //   0:右記以外、1:後発品
	    zaikei = get_csv_value(sa, 28);
		    // zaikei: 剤型
		    //   1:内服薬、3:その他、4:注射薬, 6:外用薬、8:歯科用薬剤、9:歯科特定薬剤
	    yakkacode = get_csv_value(sa, 32);
    */
    class IyakuhinRow
    {
        public string Kubun;
        public string MasterShubetsu;
        public int Iyakuhincode;
        public string Name;
        public string Yomi;
        public string Unit;
        public int KingakuShubetsu;
        public string Yakka;
        public int Madoku;
        public int Kouhatsu;
        public int Zaikei;
        public string Yakkacode;

        public IyakuhinRow(Row row)
        {
            Kubun = row.GetString(1);
            MasterShubetsu = row.GetString(2);
            Iyakuhincode = row.GetInt(3);
            Name = row.GetString(5);
            Yomi = row.GetString(7);
            Unit = row.GetString(10);
            KingakuShubetsu = row.GetInt(11);
            Yakka = row.GetString(12);
            Madoku = row.GetInt(14);
            Kouhatsu = row.GetInt(17);
            Zaikei = row.GetInt(28);
            Yakkacode = row.GetString(32);
        }
    }

    /*
	    henkou_kubun = get_csv_value(sa, 1);
	    if( *henkou_kubun == '1'  // 抹消 
            ||
		    *henkou_kubun == '9'  // 廃止  
        )
		    return 1;
	    master_shubetsu = get_csv_value(sa, 2);
	    if( *master_shubetsu != 'S' ){
		    MessageBox(NULL, "診療行為マスターのデータではありません", "エラー", MB_ICONERROR);
		    return 0;
	    }
	    nyuugaitekiyou = get_csv_value(sa, 13);
	    if( *nyuugaitekiyou == '1' ) return 1;
	    byoushinkubun = get_csv_value(sa, 19);
	    if( *byoushinkubun == '1' ) return 1;
	    tensuu_shikibetsu = get_csv_value(sa, 11);
	    if( *tensuu_shikibetsu == '0' ) return 1;
	    shinryoucode = get_csv_value(sa, 3);
	    name = get_csv_value(sa, 5);
	    tensuu = get_csv_value(sa, 12);
	    shuukeisaki = get_csv_value(sa, 15);
	    houkatsukensa = get_csv_value(sa, 16);
	    if( strlen(houkatsukensa) == 1 ){
		    buf_houkatsukensa[1] = *houkatsukensa;
		    houkatsukensa = buf_houkatsukensa;
	    }
	    oushinkubun = get_csv_value(sa, 17);
	    kensagroup = get_csv_value(sa, 51);
	    if( strlen(kensagroup) == 1 ){
		    buf_kensagroup[1] = *kensagroup;
		    kensagroup = buf_kensagroup;
	    }
	    roujintekiyou = get_csv_value(sa, 14);
	    code_shou = get_csv_value(sa, 90);
	    code_bu   = get_csv_value(sa, 91);
	    code_alpha = get_csv_value(sa, 85);
	    code_kubun = get_csv_value(sa, 92); 
    */

    class ShinryouRow
    {
        public string Kubun;
        public string MasterShubetsu;
        public string NyuuGaiTekiyou;
        public string ByouShinKubun;
        public string TensuuShikibetsu;
        public int Shinryoucode;
        public string Name;
        public string Tensuu;
        public string Shuukeisaki;
        public string Houkatsukensa;
        public string OushinKubun;
        public string KensaGroup;
        public string RoujinTekiyou;
        public string CodeShou;
        public string CodeBu;
        public string CodeAlpha;
        public string CodeKubun;

        public ShinryouRow(Row row)
        {
            Kubun = row.GetString(1);
            MasterShubetsu = row.GetString(2);
            NyuuGaiTekiyou = row.GetString(13);
            ByouShinKubun = row.GetString(19);
            TensuuShikibetsu = row.GetString(11);
            Shinryoucode = row.GetInt(3);
            Name = row.GetString(5);
            Tensuu = row.GetString(12);
            Shuukeisaki = row.GetString(15);
            Houkatsukensa = twoChars(row.GetString(16));
            OushinKubun = row.GetString(17);
            KensaGroup = twoChars(row.GetString(51));
            RoujinTekiyou = row.GetString(14);
            CodeShou = row.GetString(90);
            CodeBu = row.GetString(91);
            CodeAlpha = row.GetString(85);
            CodeKubun = row.GetString(92);
        }

        private string twoChars(string s)
        {
            if (s.Length == 1)
            {
                return "0" + s;
            }
            else
            {
                return s;
            }
        }

    }

    /*  
        henkou_kubun = get_csv_value(sa, 1);
        if( *henkou_kubun == '1'  // 抹消 
            ||
            *henkou_kubun == '9'  // 廃止 
        )
            return 1;
        master_shubetsu = get_csv_value(sa, 2);
        if( *master_shubetsu != 'T' ){
            MessageBox(NULL, "特定器材マスターのデータではありません", "エラー", MB_ICONERROR);
            return 0;
        }
        kizaicode = get_csv_value(sa, 3);
        name = get_csv_value(sa, 5);
        yomi = get_csv_value(sa, 7);
        unit = get_csv_value(sa, 10);
        kingaku_shubetsu = get_csv_value(sa, 11);
        if( *kingaku_shubetsu != '1' )
            return 1;
        kingaku = get_csv_value(sa, 12);
    */

    class KizaiRow
    {
        public string Kubun;
        public string MasterShubetsu;
        public int Kizaicode;
        public string Name;
        public string Yomi;
        public string Unit;
        public string KingakuShubetsu;
        public string Kingaku;

        public KizaiRow(Row row)
        {
            Kubun = row.GetString(1);
            MasterShubetsu = row.GetString(2);
            Kizaicode = row.GetInt(3);
            Name = row.GetString(5);
            Yomi = row.GetString(7);
            Unit = row.GetString(10);
            KingakuShubetsu = row.GetString(11);
            Kingaku = row.GetString(12);
        }
    }

}
