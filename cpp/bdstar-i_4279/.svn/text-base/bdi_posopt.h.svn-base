#ifndef BDI_POSOPT_H
#define BDI_POSOPT_H

#define NR_MAXAREA 100
#define NR_MAXPOINT	10

#ifdef arm
#define BDI_SYSPARA_FILE  "/mnt/user/config/bdi-para.conf"
#define BDI_AREACONF_FILE  "/mnt/user/config/bdi-area.conf"
#else
#define BDI_SYSPARA_FILE  "bdi-para.conf"
#define BDI_AREACONF_FILE  "bdi-area.conf"
#endif

typedef struct _bdi_pos {
	/* also used for circle area to store radius value */
	int lon;	/* Longitude, in 1/10000 min, East=positive West=Negative */
	int	lat;	/* Latitude, in 1/10000 min, North=positive South=Negative */
} bdi_pt_t;

typedef struct _bdi_area_st{
	int isalarmst;		/* alarm stat*/
	int time;	/* last alarm time */
	int isreport;
}bdi_area_st;

typedef struct _bdi_area {
	int mask;		/* mask=1 pause, =0 continue */
	int type;
	int npt;
	bdi_pt_t pt[NR_MAXPOINT];
	//pos_t fp[2];		/* fast points - surrounding rectange coordinates */
} bdi_area_t;

/* area alarm type */
enum {
	AW_OUT	= 0,
	AW_IN	= 1,
	AW_OP	= 2,
	AW_OUT_DIS	= 5,
	AW_IN_DIS	= 6,
	AW_OP_DIS	= 7,
};
#define AA_DIS(op)	((op)+5)
/* use GPS definition about lon/lat
 * Longitude, in 1/10000 min, East=positive West=Negative
 * Latitude, in 1/10000 min, North=positive South=Negative
 *
 * return: 1/10 meter
*/
int get_dist(int lon0, int lat0, int lon1, int lat1);

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
int pt_in_area(bdi_pt_t pt, bdi_area_t ar);
void initParaConfFile();
void initAreaConfFile();

#endif
