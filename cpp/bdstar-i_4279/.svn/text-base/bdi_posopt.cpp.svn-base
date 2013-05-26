#include <math.h>
#include <stdlib.h>
#include <stdio.h>

#include "bdi_posopt.h"
#include "config.h"

/* 1 minute of longitude at each latitude degree(0-90): 1/100 meters */
static const int delta_lon[] = {
	185532,
	185504,
	185420,
	185280,
	185084,
	184831,
	184523,
	184159,
	183739,
	183263,
	182732,
	182146,
	181504,
	180808,
	180057,
	179251,
	178391,
	177476,
	176508,
	175487,
	174412,
	173284,
	172104,
	170871,
	169586,
	168250,
	166863,
	165425,
	163936,
	162398,
	160810,
	159174,
	157489,
	155755,
	153975,
	152147,
	150273,
	148353,
	146387,
	144377,
	142323,
	140225,
	138085,
	135902,
	133677,
	131411,
	129105,
	126760,
	124376,
	121953,
	119493,
	116996,
	114463,
	111895,
	109293,
	106657,
	103988,
	101287,
	98555,
	95792,
	93000,
	90179,
	87330,
	84455,
	81553,
	78626,
	75675,
	72700,
	69703,
	66684,
	63644,
	60585,
	57507,
	54411,
	51299,
	48170,
	45026,
	41869,
	38699,
	35516,
	32322,
	29119,
	25906,
	22686,
	19458,
	16224,
	12985,
	9743,
	6497,
	3249,
	0,
};

/* 1 minute of latitude at each latitude degree(0-90): 1/100 meters */
static const int delta_lat[] = {
	184290,
	184291,
	184293,
	184296,
	184299,
	184305,
	184311,
	184318,
	184326,
	184336,
	184346,
	184358,
	184370,
	184384,
	184399,
	184414,
	184431,
	184449,
	184467,
	184487,
	184507,
	184528,
	184550,
	184573,
	184597,
	184621,
	184647,
	184673,
	184699,
	184726,
	184754,
	184782,
	184811,
	184841,
	184871,
	184901,
	184932,
	184963,
	184994,
	185026,
	185058,
	185090,
	185122,
	185155,
	185187,
	185220,
	185252,
	185285,
	185317,
	185350,
	185382,
	185414,
	185446,
	185477,
	185508,
	185539,
	185570,
	185600,
	185629,
	185659,
	185687,
	185715,
	185743,
	185769,
	185796,
	185821,
	185846,
	185870,
	185893,
	185915,
	185937,
	185957,
	185977,
	185996,
	186014,
	186031,
	186047,
	186061,
	186075,
	186088,
	186100,
	186111,
	186120,
	186129,
	186136,
	186142,
	186147,
	186151,
	186154,
	186156,
	186157,
};

/* use GPS definition about lon/lat
 * Longitude, in 1/10000 min, East=positive West=Negative
 * Latitude, in 1/10000 min, North=positive South=Negative
 *
 * return: 1/10 meter
*/
int get_dist(int lon0, int lat0, int lon1, int lat1)
{
	double x, y;
	int r, idx, dx, dy;

	/* 1/10000min -> round to 1 deg */
	y = (lat0 + lat1)/(2*10000.0) + 0.5;
	idx = int(y);

	dx = delta_lon[idx];	/* 1 minute length of longitude in 1/100 metres */
	dy = delta_lat[idx];	/* 1 minute length of latitude in 1/100 metres */

	x = (lon1 - lon0)/10000.0;
	y = (lat1 - lat0)/10000.0;
	x *= dx;
	y *= dy;

	r = int(sqrt(x*x+y*y)/10.0);

	return r;
}

/* CHECK WHETHER A POINT IS IN A POLYGEN AREA
 *
 * 判断原理：射线法
 *     如果要判断任意一点是否在多边形区域内，则可以从判定点向任意方向引一条射线，
 * 计算该射线与多边形的所有边的相交情况，如果相交点的个数为奇数，则该点在多边形
 * 内；如果相交点的个数为偶数，则该点在多边形外。
 *     由于地球为圆形，因此实际实现时只需从判定点引一条到南极或北极的射线，然后
 * 进行相交点的判断即可。
 * 特殊处理：
 *     当跨越经度或纬度分界点时，需要对数据进行加模操作，比如东经变西经或北纬变南纬
 *     当相交点落于多边形的顶点上时，需要做特殊判断。
 *
 * -1: outside
 *  0: on border
 *  1: inside
 *         	fp[1](E,N)
 *   +--------+
 *   |		  |
 *   |		  |
 *   |		  |
 *   |		  |
 *   +--------+
 * fp[0](W,S)
 * 上北下南 左西右东
 */
/* approXimately EQual to, Less Than, Greater Than */
#define FUZZ		10
#define xeq(a, b)	(abs((a) - (b)) < FUZZ)
#define xlt(a, b)	((a) - (b) < FUZZ)
#define xgt(a, b)	xlt(b, a)

int pt_in_area(bdi_pt_t pt, bdi_area_t ar)
{
	int v, v1, v2;
	int dlon, dlat, dx, dy;
	int i, nc;
	bdi_pt_t *pt1, *pt2;

	/* legal check */
	if(ar.npt < 1) return -1;

	/* STEP1: check fast rectangle */
	//if(xlt(pt.lon, ar.fp[0].lon) || xgt(pt.lon, ar.fp[1].lon) ||
	//xlt(pt.lat, ar.fp[0].lat) || xgt(pt.lat, ar.fp[1].lat)) return -1;

	/* STEP2: check special case: circle or rectangle area */
	if(ar.npt == 1) {
		v = get_dist(pt.lon, pt.lat, ar.pt[0].lon, ar.pt[0].lat)/10;
		v = v - ar.pt[1].lon;

		if(abs(v) < FUZZ) return 0;
		else if(v > 0) return -1;
		else return 1;
	} else if(ar.npt == 2) {
		if(xeq(pt.lon, ar.pt[0].lon) || xeq(pt.lon, ar.pt[1].lon) ||
		        xeq(pt.lat, ar.pt[0].lat) || xeq(pt.lat, ar.pt[1].lat)) return 0;
		else return 1;
	}

	/* STEP3: polygen area */
	nc = 0;

	for(i = 0; i < ar.npt; i++) {
		pt1 = &ar.pt[i];

		if(i != ar.npt - 1)
			pt2 = &ar.pt[i+1];
		else
			pt2 = &ar.pt[0];

		/* whether THE CHECK POINT is on one side of line points
		 * 假如从判定点到北极引一条射线(垂直方向，平行于经度)：
		 * a) 顶点过滤：因为经过一个顶点则一定相交两条线段，对最终结果无影响
		 * b) 经度过滤：如果判定点在线段的两个端点的同一侧，则此点到北极的射线不会与线段相交
		 * c) 纬度过滤：如果判定点小于任何一个端点，则线段位于判定点南侧，不会与线段相交
		 *              如果两个端点均在判定点和北极之间，则必然会相交，可不用计算
		 * d) 其它情况：求出判定点经度在线段上的投影，进行纬度比较判断是否相交
		 *      |      |
		 *		|	   * pt2
		 *		|	  /|
		 * pt . |  . / | .
		 *		|	/  |
		 *      |  /   |
		 *      | /    |
		 *      |/     |
		 *      * pt1  |
		 *      |      |
		 * TODO 经度跨越180或纬度跨越0的情况需特殊处理
		 *      如果跨越经度180，则dlon = lon1-lon2 > 180*10000, 此时用360*10000 - dlon即可(同理dx)
		 *      如果跨越纬度0，对计算结果无影响
		 */
		/* a): right on points */
		if((xeq(pt.lon, pt1->lon) && xeq(pt.lat, pt1->lat)) ||
		        (xeq(pt.lon, pt2->lon) && xeq(pt.lat, pt2->lat)) ) return 0;

		/* b): longitude: CHECK POINT is between pt1 and pt2 */
		v1 = (pt.lon > pt1->lon) + (pt.lon > pt2->lon);

		/* both false or ture */
		if(v1 == 0 || v1 == 2) continue;

		/* c): latitude: at least one line point is between CHECK POINT and pole(90 deg) */
		v2 = xgt(pt.lat, pt1->lat) + xgt(pt.lat, pt2->lat);

		/* both false, cannot cross */
		if(v2 == 0) continue;

		/* both true, must cross */
		if(v2 == 2) {
			nc++;
			continue;
		}

		/* d): calculate projection */
		dlon = abs(pt2->lon - pt1->lon);

		/* in 1/10000 degree */
		if(dlon > 1800000) dlon = 3600000 - dlon;

		dx = abs(pt.lon - pt1->lon);

		if(dx > 1800000) dx = 3600000 - dx;

		/* keep sign for dy calculation */
		dlat = pt2->lat - pt1->lat;
		dy = dx * dlat / dlon;
		/* crosspoint latitude */
		v = pt1->lat + dy;

		if(xlt(v, pt.lat)) continue;

		/* find cross */
		nc++;
	}

	/* odd cross points, inside */
	if(nc & 1) return 1;

	/* otherwise, outside */
	return -1;
}

void initParaConfFile()
{
	BDI::Config conf;
	conf.open(BDI_SYSPARA_FILE); 
	conf.setGroup("SYSPARA");
	conf.setValue("serverState", 1);
	conf.setValue("sleepState", 0);
	conf.setValue("ref_PosReport", 6);
	conf.setValue("ref_PosCol1", 1);
	conf.setValue("ref_PosCol2", 12);
	conf.setValue("ref_GpsFix", 2);
	conf.setValue("thv_course", 15);
	conf.setValue("thv_speedZero", 2);
	conf.setValue("thv_speedRun", 10);
	conf.setValue("thv_posDiff", 20);
	conf.save();
}

void initAreaConfFile()
{
	FILE *f = fopen(BDI_AREACONF_FILE, "w");
	if (NULL == f){
		fprintf(stderr, "init area config file error!\r\n");
		return ;
	}
	bdi_area_t area;

	area.mask = 1;

	for(int i = 0; i < NR_MAXAREA; i++){
		if(fwrite(&area, sizeof(bdi_area_t), 1, f) == 0)
			return;
	}
	return ;
}

