package com.navchina.xzhou.world;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WorldWikiActivity extends ListActivity
{
	static private ArrayList<Country> EU = new ArrayList<Country>();
    
    static {
        EU.add(new Country(R.string.ad, R.drawable.ad, R.string.ad_url));
        EU.add(new Country(R.string.ae, R.drawable.ae, R.string.ae_url));
        EU.add(new Country(R.string.af, R.drawable.af, R.string.af_url));
        EU.add(new Country(R.string.ag, R.drawable.ag, R.string.ag_url));
        EU.add(new Country(R.string.ai, R.drawable.ai, R.string.ai_url));
        EU.add(new Country(R.string.al, R.drawable.al, R.string.al_url));
        EU.add(new Country(R.string.am, R.drawable.am, R.string.am_url));
        EU.add(new Country(R.string.an, R.drawable.an, R.string.an_url));
        EU.add(new Country(R.string.ao, R.drawable.ao, R.string.ao_url));
        EU.add(new Country(R.string.ar, R.drawable.ar, R.string.ar_url));
        EU.add(new Country(R.string.as, R.drawable.as, R.string.as_url));
        EU.add(new Country(R.string.at, R.drawable.at, R.string.at_url));
        EU.add(new Country(R.string.au, R.drawable.au, R.string.au_url));
        EU.add(new Country(R.string.aw, R.drawable.aw, R.string.aw_url));
        EU.add(new Country(R.string.az, R.drawable.az, R.string.az_url));
        EU.add(new Country(R.string.ba, R.drawable.ba, R.string.ba_url));
        EU.add(new Country(R.string.bb, R.drawable.bb, R.string.bb_url));
        EU.add(new Country(R.string.bd, R.drawable.bd, R.string.bd_url));
        EU.add(new Country(R.string.be, R.drawable.be, R.string.be_url));
        EU.add(new Country(R.string.bf, R.drawable.bf, R.string.bf_url));
        EU.add(new Country(R.string.bg, R.drawable.bg, R.string.bg_url));
        EU.add(new Country(R.string.bh, R.drawable.bh, R.string.bh_url));
        EU.add(new Country(R.string.bi, R.drawable.bi, R.string.bi_url));
        EU.add(new Country(R.string.bj, R.drawable.bj, R.string.bj_url));
        EU.add(new Country(R.string.bm, R.drawable.bm, R.string.bm_url));
        EU.add(new Country(R.string.bn, R.drawable.bn, R.string.bn_url));
        EU.add(new Country(R.string.bo, R.drawable.bo, R.string.bo_url));
        EU.add(new Country(R.string.br, R.drawable.br, R.string.br_url));
        EU.add(new Country(R.string.bs, R.drawable.bs, R.string.bs_url));
        EU.add(new Country(R.string.bt, R.drawable.bt, R.string.bt_url));
        EU.add(new Country(R.string.bv, R.drawable.bv, R.string.bv_url));
        EU.add(new Country(R.string.bw, R.drawable.bw, R.string.bw_url));
        EU.add(new Country(R.string.by, R.drawable.by, R.string.by_url));
        EU.add(new Country(R.string.bz, R.drawable.bz, R.string.bz_url));
        EU.add(new Country(R.string.ca, R.drawable.ca, R.string.ca_url));
        EU.add(new Country(R.string.catalonia, R.drawable.catalonia, R.string.catalonia_url));
        EU.add(new Country(R.string.cc, R.drawable.cc, R.string.cc_url));
        EU.add(new Country(R.string.cd, R.drawable.cd, R.string.cd_url));
        EU.add(new Country(R.string.cf, R.drawable.cf, R.string.cf_url));
        EU.add(new Country(R.string.cg, R.drawable.cg, R.string.cg_url));
        EU.add(new Country(R.string.ch, R.drawable.ch, R.string.ch_url));
        EU.add(new Country(R.string.ci, R.drawable.ci, R.string.ci_url));
        EU.add(new Country(R.string.ck, R.drawable.ck, R.string.ck_url));
        EU.add(new Country(R.string.cl, R.drawable.cl, R.string.cl_url));
        EU.add(new Country(R.string.cm, R.drawable.cm, R.string.cm_url));
        EU.add(new Country(R.string.cn, R.drawable.cn, R.string.cn_url));
        EU.add(new Country(R.string.co, R.drawable.co, R.string.co_url));
        EU.add(new Country(R.string.cr, R.drawable.cr, R.string.cr_url));
        EU.add(new Country(R.string.cu, R.drawable.cu, R.string.cu_url));
        EU.add(new Country(R.string.cv, R.drawable.cv, R.string.cv_url));
        EU.add(new Country(R.string.cx, R.drawable.cx, R.string.cx_url));
        EU.add(new Country(R.string.cy, R.drawable.cy, R.string.cy_url));
        EU.add(new Country(R.string.cz, R.drawable.cz, R.string.cz_url));
        EU.add(new Country(R.string.de, R.drawable.de, R.string.de_url));
        EU.add(new Country(R.string.dj, R.drawable.dj, R.string.dj_url));
        EU.add(new Country(R.string.dk, R.drawable.dk, R.string.dk_url));
        EU.add(new Country(R.string.dm, R.drawable.dm, R.string.dm_url));
        EU.add(new Country(R.string.dz, R.drawable.dz, R.string.dz_url));
        EU.add(new Country(R.string.ec, R.drawable.ec, R.string.ec_url));
        EU.add(new Country(R.string.ee, R.drawable.ee, R.string.ee_url));
        EU.add(new Country(R.string.eg, R.drawable.eg, R.string.eg_url));
        EU.add(new Country(R.string.eh, R.drawable.eh, R.string.eh_url));
        EU.add(new Country(R.string.england, R.drawable.england, R.string.england_url));
        EU.add(new Country(R.string.er, R.drawable.er, R.string.er_url));
        EU.add(new Country(R.string.es, R.drawable.es, R.string.es_url));
        EU.add(new Country(R.string.et, R.drawable.et, R.string.et_url));
        EU.add(new Country(R.string.fi, R.drawable.fi, R.string.fi_url));
        EU.add(new Country(R.string.fj, R.drawable.fj, R.string.fj_url));
        EU.add(new Country(R.string.fk, R.drawable.fk, R.string.fk_url));
        EU.add(new Country(R.string.fm, R.drawable.fm, R.string.fm_url));
        EU.add(new Country(R.string.fo, R.drawable.fo, R.string.fo_url));
        EU.add(new Country(R.string.fr, R.drawable.fr, R.string.fr_url));
        EU.add(new Country(R.string.galicia, R.drawable.galicia, R.string.galicia_url));
        EU.add(new Country(R.string.ga, R.drawable.ga, R.string.ga_url));
        EU.add(new Country(R.string.gb, R.drawable.gb, R.string.gb_url));
        EU.add(new Country(R.string.gd, R.drawable.gd, R.string.gd_url));
        EU.add(new Country(R.string.ge, R.drawable.ge, R.string.ge_url));
        EU.add(new Country(R.string.gf, R.drawable.gf, R.string.gf_url));
        EU.add(new Country(R.string.gg, R.drawable.gg, R.string.gg_url));
        EU.add(new Country(R.string.gh, R.drawable.gh, R.string.gh_url));
        EU.add(new Country(R.string.gi, R.drawable.gi, R.string.gi_url));
        EU.add(new Country(R.string.gl, R.drawable.gl, R.string.gl_url));
        EU.add(new Country(R.string.gm, R.drawable.gm, R.string.gm_url));
        EU.add(new Country(R.string.gn, R.drawable.gn, R.string.gn_url));
        EU.add(new Country(R.string.gp, R.drawable.gp, R.string.gp_url));
        EU.add(new Country(R.string.gq, R.drawable.gq, R.string.gq_url));
        EU.add(new Country(R.string.gr, R.drawable.gr, R.string.gr_url));
        EU.add(new Country(R.string.gs, R.drawable.gs, R.string.gs_url));
        EU.add(new Country(R.string.gt, R.drawable.gt, R.string.gt_url));
        EU.add(new Country(R.string.gu, R.drawable.gu, R.string.gu_url));
        EU.add(new Country(R.string.gw, R.drawable.gw, R.string.gw_url));
        EU.add(new Country(R.string.gy, R.drawable.gy, R.string.gy_url));
        EU.add(new Country(R.string.hk, R.drawable.hk, R.string.hk_url));
        EU.add(new Country(R.string.hm, R.drawable.hm, R.string.hm_url));
        EU.add(new Country(R.string.hn, R.drawable.hn, R.string.hn_url));
        EU.add(new Country(R.string.hr, R.drawable.hr, R.string.hr_url));
        EU.add(new Country(R.string.ht, R.drawable.ht, R.string.ht_url));
        EU.add(new Country(R.string.hu, R.drawable.hu, R.string.hu_url));
        EU.add(new Country(R.string.id, R.drawable.id, R.string.id_url));
        EU.add(new Country(R.string.ie, R.drawable.ie, R.string.ie_url));
        EU.add(new Country(R.string.il, R.drawable.il, R.string.il_url));
        EU.add(new Country(R.string.im, R.drawable.im, R.string.im_url));
        EU.add(new Country(R.string.in, R.drawable.in, R.string.in_url));
        EU.add(new Country(R.string.io, R.drawable.io, R.string.io_url));
        EU.add(new Country(R.string.iq, R.drawable.iq, R.string.iq_url));
        EU.add(new Country(R.string.ir, R.drawable.ir, R.string.ir_url));
        EU.add(new Country(R.string.is, R.drawable.is, R.string.is_url));
        EU.add(new Country(R.string.it, R.drawable.it, R.string.it_url));
        EU.add(new Country(R.string.je, R.drawable.je, R.string.je_url));
        EU.add(new Country(R.string.jm, R.drawable.jm, R.string.jm_url));
        EU.add(new Country(R.string.jo, R.drawable.jo, R.string.jo_url));
        EU.add(new Country(R.string.jp, R.drawable.jp, R.string.jp_url));
        EU.add(new Country(R.string.ke, R.drawable.ke, R.string.ke_url));
        EU.add(new Country(R.string.kg, R.drawable.kg, R.string.kg_url));
        EU.add(new Country(R.string.kh, R.drawable.kh, R.string.kh_url));
        EU.add(new Country(R.string.ki, R.drawable.ki, R.string.ki_url));
        EU.add(new Country(R.string.km, R.drawable.km, R.string.km_url));
        EU.add(new Country(R.string.kn, R.drawable.kn, R.string.kn_url));
        EU.add(new Country(R.string.kp, R.drawable.kp, R.string.kp_url));
        EU.add(new Country(R.string.kr, R.drawable.kr, R.string.kr_url));
        EU.add(new Country(R.string.kw, R.drawable.kw, R.string.kw_url));
        EU.add(new Country(R.string.ky, R.drawable.ky, R.string.ky_url));
        EU.add(new Country(R.string.kz, R.drawable.kz, R.string.kz_url));
        EU.add(new Country(R.string.la, R.drawable.la, R.string.la_url));
        EU.add(new Country(R.string.lb, R.drawable.lb, R.string.lb_url));
        EU.add(new Country(R.string.lc, R.drawable.lc, R.string.lc_url));
        EU.add(new Country(R.string.li, R.drawable.li, R.string.li_url));
        EU.add(new Country(R.string.lk, R.drawable.lk, R.string.lk_url));
        EU.add(new Country(R.string.lr, R.drawable.lr, R.string.lr_url));
        EU.add(new Country(R.string.ls, R.drawable.ls, R.string.ls_url));
        EU.add(new Country(R.string.lt, R.drawable.lt, R.string.lt_url));
        EU.add(new Country(R.string.lu, R.drawable.lu, R.string.lu_url));
        EU.add(new Country(R.string.lv, R.drawable.lv, R.string.lv_url));
        EU.add(new Country(R.string.ly, R.drawable.ly, R.string.ly_url));
        EU.add(new Country(R.string.ma, R.drawable.ma, R.string.ma_url));
        EU.add(new Country(R.string.mc, R.drawable.mc, R.string.mc_url));
        EU.add(new Country(R.string.md, R.drawable.md, R.string.md_url));
        EU.add(new Country(R.string.me, R.drawable.me, R.string.me_url));
        EU.add(new Country(R.string.mg, R.drawable.mg, R.string.mg_url));
        EU.add(new Country(R.string.mh, R.drawable.mh, R.string.mh_url));
        EU.add(new Country(R.string.mk, R.drawable.mk, R.string.mk_url));
        EU.add(new Country(R.string.ml, R.drawable.ml, R.string.ml_url));
        EU.add(new Country(R.string.mm, R.drawable.mm, R.string.mm_url));
        EU.add(new Country(R.string.mn, R.drawable.mn, R.string.mn_url));
        EU.add(new Country(R.string.mo, R.drawable.mo, R.string.mo_url));
        EU.add(new Country(R.string.mp, R.drawable.mp, R.string.mp_url));
        EU.add(new Country(R.string.mq, R.drawable.mq, R.string.mq_url));
        EU.add(new Country(R.string.mr, R.drawable.mr, R.string.mr_url));
        EU.add(new Country(R.string.ms, R.drawable.ms, R.string.ms_url));
        EU.add(new Country(R.string.mt, R.drawable.mt, R.string.mt_url));
        EU.add(new Country(R.string.mu, R.drawable.mu, R.string.mu_url));
        EU.add(new Country(R.string.mv, R.drawable.mv, R.string.mv_url));
        EU.add(new Country(R.string.mw, R.drawable.mw, R.string.mw_url));
        EU.add(new Country(R.string.mx, R.drawable.mx, R.string.mx_url));
        EU.add(new Country(R.string.my, R.drawable.my, R.string.my_url));
        EU.add(new Country(R.string.mz, R.drawable.mz, R.string.mz_url));
        EU.add(new Country(R.string.na, R.drawable.na, R.string.na_url));
        EU.add(new Country(R.string.nc2, R.drawable.nc2, R.string.nc2_url));
        EU.add(new Country(R.string.nc, R.drawable.nc, R.string.nc_url));
        EU.add(new Country(R.string.ne, R.drawable.ne, R.string.ne_url));
        EU.add(new Country(R.string.nf, R.drawable.nf, R.string.nf_url));
        EU.add(new Country(R.string.ng, R.drawable.ng, R.string.ng_url));
        EU.add(new Country(R.string.ni, R.drawable.ni, R.string.ni_url));
        EU.add(new Country(R.string.nl, R.drawable.nl, R.string.nl_url));
        EU.add(new Country(R.string.no, R.drawable.no, R.string.no_url));
        EU.add(new Country(R.string.np, R.drawable.np, R.string.np_url));
        EU.add(new Country(R.string.nr, R.drawable.nr, R.string.nr_url));
        EU.add(new Country(R.string.nu, R.drawable.nu, R.string.nu_url));
        EU.add(new Country(R.string.nz, R.drawable.nz, R.string.nz_url));
        EU.add(new Country(R.string.om, R.drawable.om, R.string.om_url));
        EU.add(new Country(R.string.pa, R.drawable.pa, R.string.pa_url));
        EU.add(new Country(R.string.pe, R.drawable.pe, R.string.pe_url));
        EU.add(new Country(R.string.pf, R.drawable.pf, R.string.pf_url));
        EU.add(new Country(R.string.pg, R.drawable.pg, R.string.pg_url));
        EU.add(new Country(R.string.ph, R.drawable.ph, R.string.ph_url));
        EU.add(new Country(R.string.pk, R.drawable.pk, R.string.pk_url));
        EU.add(new Country(R.string.pl, R.drawable.pl, R.string.pl_url));
        EU.add(new Country(R.string.pm, R.drawable.pm, R.string.pm_url));
        EU.add(new Country(R.string.pn, R.drawable.pn, R.string.pn_url));
        EU.add(new Country(R.string.pr, R.drawable.pr, R.string.pr_url));
        EU.add(new Country(R.string.ps, R.drawable.ps, R.string.ps_url));
        EU.add(new Country(R.string.pt, R.drawable.pt, R.string.pt_url));
        EU.add(new Country(R.string.pw, R.drawable.pw, R.string.pw_url));
        EU.add(new Country(R.string.py, R.drawable.py, R.string.py_url));
        EU.add(new Country(R.string.qa, R.drawable.qa, R.string.qa_url));
        EU.add(new Country(R.string.re, R.drawable.re, R.string.re_url));
        EU.add(new Country(R.string.ro, R.drawable.ro, R.string.ro_url));
        EU.add(new Country(R.string.rs, R.drawable.rs, R.string.rs_url));
        EU.add(new Country(R.string.ru, R.drawable.ru, R.string.ru_url));
        EU.add(new Country(R.string.rw, R.drawable.rw, R.string.rw_url));
        EU.add(new Country(R.string.sa, R.drawable.sa, R.string.sa_url));
        EU.add(new Country(R.string.sb, R.drawable.sb, R.string.sb_url));
        EU.add(new Country(R.string.scotland, R.drawable.scotland, R.string.scotland_url));
        EU.add(new Country(R.string.sc, R.drawable.sc, R.string.sc_url));
        EU.add(new Country(R.string.sd, R.drawable.sd, R.string.sd_url));
        EU.add(new Country(R.string.se, R.drawable.se, R.string.se_url));
        EU.add(new Country(R.string.sg, R.drawable.sg, R.string.sg_url));
        EU.add(new Country(R.string.sh, R.drawable.sh, R.string.sh_url));
        EU.add(new Country(R.string.si, R.drawable.si, R.string.si_url));
        EU.add(new Country(R.string.sj, R.drawable.sj, R.string.sj_url));
        EU.add(new Country(R.string.sk, R.drawable.sk, R.string.sk_url));
        EU.add(new Country(R.string.sl, R.drawable.sl, R.string.sl_url));
        EU.add(new Country(R.string.sm, R.drawable.sm, R.string.sm_url));
        EU.add(new Country(R.string.sn, R.drawable.sn, R.string.sn_url));
        EU.add(new Country(R.string.so, R.drawable.so, R.string.so_url));
        EU.add(new Country(R.string.sr, R.drawable.sr, R.string.sr_url));
        EU.add(new Country(R.string.st, R.drawable.st, R.string.st_url));
        EU.add(new Country(R.string.sv, R.drawable.sv, R.string.sv_url));
        EU.add(new Country(R.string.sy, R.drawable.sy, R.string.sy_url));
        EU.add(new Country(R.string.sz, R.drawable.sz, R.string.sz_url));
        EU.add(new Country(R.string.tc, R.drawable.tc, R.string.tc_url));
        EU.add(new Country(R.string.td, R.drawable.td, R.string.td_url));
        EU.add(new Country(R.string.tf, R.drawable.tf, R.string.tf_url));
        EU.add(new Country(R.string.tg, R.drawable.tg, R.string.tg_url));
        EU.add(new Country(R.string.th, R.drawable.th, R.string.th_url));
        EU.add(new Country(R.string.tj, R.drawable.tj, R.string.tj_url));
        EU.add(new Country(R.string.tk, R.drawable.tk, R.string.tk_url));
        EU.add(new Country(R.string.tl, R.drawable.tl, R.string.tl_url));
        EU.add(new Country(R.string.tm, R.drawable.tm, R.string.tm_url));
        EU.add(new Country(R.string.tn, R.drawable.tn, R.string.tn_url));
        EU.add(new Country(R.string.to, R.drawable.to, R.string.to_url));
        EU.add(new Country(R.string.tr, R.drawable.tr, R.string.tr_url));
        EU.add(new Country(R.string.tt, R.drawable.tt, R.string.tt_url));
        EU.add(new Country(R.string.tv, R.drawable.tv, R.string.tv_url));
        EU.add(new Country(R.string.tw, R.drawable.tw, R.string.tw_url));
        EU.add(new Country(R.string.tz, R.drawable.tz, R.string.tz_url));
        EU.add(new Country(R.string.ua, R.drawable.ua, R.string.ua_url));
        EU.add(new Country(R.string.ug, R.drawable.ug, R.string.ug_url));
        EU.add(new Country(R.string.um, R.drawable.um, R.string.um_url));
        EU.add(new Country(R.string.us, R.drawable.us, R.string.us_url));
        EU.add(new Country(R.string.uy, R.drawable.uy, R.string.uy_url));
        EU.add(new Country(R.string.uz, R.drawable.uz, R.string.uz_url));
        EU.add(new Country(R.string.va, R.drawable.va, R.string.va_url));
        EU.add(new Country(R.string.vc, R.drawable.vc, R.string.vc_url));
        EU.add(new Country(R.string.ve, R.drawable.ve, R.string.ve_url));
        EU.add(new Country(R.string.vg, R.drawable.vg, R.string.vg_url));
        EU.add(new Country(R.string.vi, R.drawable.vi, R.string.vi_url));
        EU.add(new Country(R.string.vn, R.drawable.vn, R.string.vn_url));
        EU.add(new Country(R.string.vu, R.drawable.vu, R.string.vu_url));
        EU.add(new Country(R.string.wales, R.drawable.wales, R.string.wales_url));
        EU.add(new Country(R.string.wf, R.drawable.wf, R.string.wf_url));
        EU.add(new Country(R.string.ws, R.drawable.ws, R.string.ws_url));
        EU.add(new Country(R.string.ye, R.drawable.ye, R.string.ye_url));
        EU.add(new Country(R.string.yt, R.drawable.yt, R.string.yt_url));
        EU.add(new Country(R.string.za, R.drawable.za, R.string.za_url));
        EU.add(new Country(R.string.zm, R.drawable.zm, R.string.zm_url));
        EU.add(new Country(R.string.zw, R.drawable.zw, R.string.zw_url));
    }

	private WebView browser;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        
        browser = (WebView)findViewById(R.id.browser);
		
        setListAdapter(new CountryAdapter());
        if (null != browser) {
            String url = "http://zh.wikipedia.org/wiki/" + "UN";
            browser.loadUrl(url);
        }
	}
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
//        String url = "http://google.com.hk/search?&q=" + getString(EU.get(position).url);
//        String url = "http://www1.baidu.com/s?cl=3&wd=" + getString(EU.get(position).url);
        String url = "http://zh.wikipedia.org/wiki/" + getString(EU.get(position).name);
                
        if(null == browser){
            //仅针对竖屏
            System.out.println("null == browser"); 
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        else {
            browser.loadUrl(url);
        }
	}

	static class Country
	{
		int name;
		int flag;
		int url;

		public Country(int name, int flag, int url)
		{
			super();
			this.name = name;
			this.flag = flag;
			this.url = url;
		}
	}
    
	class CountryAdapter extends ArrayAdapter<Country> {
	    public CountryAdapter()
        {
            super(WorldWikiActivity.this, R.layout.row, R.id.name, EU);
        }

		@Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            CountryWrapper wrapper = null;
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.row, null);
                wrapper = new CountryWrapper(convertView);
                convertView.setTag(wrapper);
            }
            else {
                wrapper = (CountryWrapper)convertView.getTag();
            }
            
            wrapper.populateFrom(getItem(position));
            return (convertView);
        }	
	}
    
	class CountryWrapper {
	    private TextView name = null;	
	    private TextView url = null;	
        private ImageView flag = null;
        private View row = null;
        
        public CountryWrapper(View row)
        {
            this.row = row;    
        }
        
        TextView getName() {
            if (null == name ) {
                name = (TextView)row.findViewById(R.id.name);
            }
            return name;
        }
        
        TextView getUrl() {
            if (null == url) {
                url = (TextView)row.findViewById(R.id.url);
            }
            return url;
        }
        
        ImageView getFlag() {
            if (null == flag) {
                flag = (ImageView)row.findViewById(R.id.flag);
            }
            return flag;	
        }
        
        void populateFrom(Country nation) {
            getName().setText(nation.name);
            getFlag().setImageResource(nation.flag);
            getUrl().setText(nation.url);
        }
	}
}