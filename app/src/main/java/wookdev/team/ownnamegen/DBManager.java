package wookdev.team.ownnamegen;

/**
 * Created by wook on 2017-04-25.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

//DB를 총괄관리
public class DBManager {

    // DB관련 상수 선언
    private static final String DB_NAME_CH = "ch_letters.db";
    private static final String DB_NAME_MANSE = "cal_manse_new.db";
    private static final String DB_MANSE_TABLE_NAME = "c_data";
    private static final String DB_CH_TABLE_NAME = "HanjaList";
    private static final String TAG = "DBManager";

    public static final int dbVersion = 1;
    public static final int TYPE_FAMILY_NAME = 2;
    public static final int TYPE_NORMAL_NAME = 1;

    // DB관련 객체 선언
    private OpenHelper opener_ch; // DB opener
    private OpenHelper opener_manse; // DB opener
    private SQLiteDatabase db_ch; // DB controller
    private SQLiteDatabase db_manse; // DB controller

    // 부가적인 객체들
    private Context context;
    private String DB_PATH;

    private Suri81[] suri81s = {
            new Suri81(1, "태초격", "새로운 것을 만들어 내는데 탁월한 재능을 갖추고 있어 타인이 생각 하지 못했던 일들을 계획하고 추진해 큰 부귀영화를 누리게 됩니다. " +
                    "하지만 뛰어난 역량에 자만 할 수 있으므로 항상 타인의 말을 귀담아 듣고 협력하고자 하는 마음 가짐을 지니도록 노력해야 합니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(2, "분산격", "의지가 박약해 어떠한 일을 시작하더라도 마무리 하지 못하고 불확실한 미래에 대한 걱정과 근심으로 가득해 어려운 삶을 살아가게 됩니다. " +
                    "또한 대인 관계가 좋지 못하여 가정과 사회에서 주변 사람들과 서로 다투며 불화가 끊이지 않아 고독한 삶을 살아가게 됩니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(3, "명예격", "명석한 두뇌와 현명함 그리고 강인한 의지를 갖추고 있어 본인이 이루고자 하는 모든 꿈을 막힘 없이 이뤄내 큰 성공을 이루게 됩니다. " +
                    "가족간의 관계가 좋아 화목하고 평안한 가정을 꾸리게 되며 대인 관계 또한 원만해 많은 사람들이 따르며 주변 사람들의 칭송을 받는 사람으로 성장해 갑니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(4,"부정격" ,"의지가 약하고 우유 부단하여 일을 하고자 하는데 있어 결단을 내리지 못하며 시작했더라도 마무리를 잘 하지 못해 본인의 자산과 역량을 허비하는 삶을 살아가게 됩니다. " +
                    "대인 관계 또한 원만치 못해 고독하며 가족 간에도 많은 불화가 있을 수 있습니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(5, "정성격" ,"타고난 성품이 어질며 덕을 갖추고 있어 항상 타의 모범이 되는 삶을 살아가며 사회의 지도자로서 주위 사람들의 신망과 칭송을 받게됩니다. " +
                    "좋은 대인 관계를 바탕으로 주변 사람들과 도움을 주고 받아 큰 성공을 이루게되며 이렇게 이룬 성공이 후세까지 이어지는 다복한 삶을 살아갑니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(7, "계승격" ,"선대가 이루어 놓은 부와 명예를 이어 받고 이를 발전 시켜 더 큰 업적을 이뤄내며 살아가는데 있어 항상 복이 함께 합니다. " +
                    "성격이 온화하며 만인과 우애를 쌓아가는 성품을 지녀 주변 사람들의 사랑을 받고 화목한 가정을 꾸리게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(8, "발달격" ,"강인한 의지와 인내심을 바탕으로 어떠한 난관도 극복해 내며 노력에 따라 지속적으로 발전해 자신의 꿈을 반드시 이뤄내고 큰 성공을 이뤄내게 됩니다. " +
                    "성격이 강한 편이므로 자신의 마음을 다스리고 온화한 성품을 지니도록 노력한다면 주변 사람들과의 관계 또한 원만해 질 수 있습니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(9, "궁박격" ,"타고난 총명함과 재주를 갖추고 있으나 의지가 부족해 본인의 꿈을 펼칠 기회가 적고 실현해 내지 못할 가능성이 많습니다. " +
                    "결혼이 늦어 질 수 있으며 가족간의 관계 또한 좋지 못 할 가능성이 많습니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(10, "공허격" ,"재능과 원대한 포부를 지녔으나 예기치 못한 어려움이 많이 발생해 실제로 본인의 역량을 제대로 사용하지 못하고 하고자 하는 일들이 실패하는 경향이 강합니다. " +
                    "결혼이 늦어지는 경우가 많고 빈번한 난관에 따라 박복한 삶을 살 가능성이 다분합니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(11, "신성격" ,"타고난 천성이 근면하며 두뇌가 남달리 뛰어나 뜻하는 모든 바를 순탄히 이뤄내며 가문을 크게 일으키고 큰 부를 쌓습니다. " +
                    "성품이 어질고 근면해 본인의 책무를 다하며 그 결과 주변 사람들의 신뢰와 존경을 받게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(12, "박약격" ,"생각이 많으며 예술적 감각이 뛰어나나 심신이 유약해 중도에 일을 포기하고 좌절을 맛보게 됩니다. " +
                    "내성적인 성격으로 타인과의 관계 또한 좋지 못해 주변 사람들의 도움을 받지 못하는 편이며 가족운도 적어 고독한 삶을 살아갈 가능성이 많습니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(13, "지모격" ,"두뇌가 명석하며 뛰어난 재능을 갖추고 있어 이러한 역량을 잘 활용한다면 어떠한 역경도 순탄히 이겨 내고 큰 성공을 이뤄내 타인의 존경을 한 몸에 받는 인물로 성장하게 됩니다. " +
                    "가정운과 부부운 또한 좋아 가정이 평안하며 윤택한 삶을 누리게 되나 자만심에 빠지는 상황에 대해서는 주의가 요구됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(14, "이산격" ,"타고난 성품은 선하나 소극적이며 노력에 비해 결실이 적어 항상 궁핍한 삶을 살며 건강 또한 좋지 않을 수 있습니다. " +
                    "소극적 성품은 타인과의 관계에도 악영향을 미치며 가족끼리 헤어져 살거나 이별하게 될 가능성도 많습니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(15, "통솔격" ,"지덕을 겸비해 주변에 따르는 사람들이 많으며 어떠한 역경도 슬기롭게 극복하고 큰 부귀영화를 얻게 됩니다. " +
                    "하고자 하는 모든 일들이 순탄하게 풀리며 부부간에 화목하고 대인 관계 측면에서도 큰 성공을 이뤄낼 수 있습니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(16, "덕망격" ,"성품이 어질고 너그러워 어려운 사람들을 도우며 큰 덕을 쌓고 이를 바탕으로 다른 사람들의 존경과 도움을 받아 본인 역시 큰 성공을 이루게 됩니다. " +
                    "인자하고 다정 다감한 성격으로 주변에 따르는 사람들이 많으며 정치와 관련된 분야에서 큰 성공을 이룰 수 있으나 자만심에 빠지는 것은 조심해야 합니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(17, "건창격" ,"강인한 의지와 인내심을 갖추고 있으며 매사에 적극적인 성격으로 하고자 하는 모든 일을 활기차게 이뤄낼 수 있습니다. " +
                    "강인한 성격은 타인과의 융화 측면에서 어려움이 존재할 수 있으므로 자신의 성공을 타인과 나누고 남을 이해하려는 성품을 갖추도록 노력해야 합니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(18, "발전격" ,"강한 의지를 갖추고 있으며 두뇌 또한 명석해 본인이 이루고자 하는 목적을 손쉽게 달성하고 자신의 분야에서 명성을 떨치게 됩니다. " +
                    "뛰어난 역량에 대한 자만심은 타인을 업신여겨 주변 사람들과의 관계를 해칠 수 있으므로 행동에 유념하며 항상 겸손한 마음 가짐을 지니며 살아가도록 노력해야 합니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(19, "고난격" ,"명석한 두뇌와 뛰어난 재능을 갖추고 있지만 예상하지 못한 어려움이 자주 발생해 그 결실이 부족한 편입니다. " +
                    "가족간에 불화가 많고 대인 관계 또한 원만하지 못해 인간 관계가 단절되고 고독하게 살아갈 가능성이 많습니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(20, "허망격" ,"본인의 자질은 뛰어나고 원대한 포부를 갖추고 있지만 큰 고난과 역경이 빈번하게 일어나 실패를 맛보고 좌절하게 됩니다. " +
                    "주변과의 인연이나 가족과의 관계 또한 좋지 못하며 항상 건강에 유념하는 삶을 살아가야 합니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(21, "자립격" ,"두뇌가 명석하고 적극적인 성격을 갖추고 있어 주변 사람들을 이끄는 리더로서 성장하게 되며 큰 부귀영화를 얻을 수 있습니다. " +
                    "초년에는 약간의 어려움을 겪을 수도 있지만 원만한 대인 관계로 주변 사람들의 도움을 받아 잘 극복하며 타인의 신망과 존경을 받는 인물로 성장하게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(22, "중절격" ,"의지가 박약해 하고자 하는 것들이 중간에 용두사미 식으로 흐지부지 하게 마무리 되는 편입니다. " +
                    "항상 주변 환경에 대한 불만과 근심이 많은 편이며 자립심이 부족하고 타인에게 의존하려는 성향이 강한 편입니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(23, "공명격" ,"항상 진취적이며 지덕과 문무를 겸비해 본인의 큰 뜻을 펼쳐 이뤄내고 큰 부귀영화를 누리게 됩니다. " +
                    "인덕 또한 풍부해 주변의 좋은 사람들이 많이 따르고 사회에서 큰 신망과 존경을 받는 인물로 성장하게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(24, "출세격" ,"두뇌가 명석하고 의지가 강해 본인이 이루고자 하는 모든 것들을 이루며 재물을 쌓아 대대손손 부귀영화를 누릴 수 있습니다. " +
                    "성품 또한 온화하며 친화력이 좋아 주위 사람들의 신망을 얻으며 사회적으로도 존경 받는 인물로 성장하게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(25, "안강격" ,"꾸준히 노력하는 근면성과 업무를 계획적으로 수행해 나가는 추진력을 갖추고 있으며 이를 바탕으로 본인이 이루고자 하는 모든 것들을 큰 어려움 없이 이뤄냅니다. " +
                    "온화한 성품으로 대인 관계도 원만하여 타인으로부터 칭송을 받으며 행복한 가정을 이루게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(25, "안강격" ,"꾸준히 노력하는 근면성과 업무를 계획적으로 수행해 나가는 추진력을 갖추고 있으며 이를 바탕으로 본인이 이루고자 하는 모든 것들을 큰 어려움 없이 이뤄냅니다. " +
                    "온화한 성품으로 대인 관계도 원만하여 타인으로부터 칭송을 받으며 행복한 가정을 이루게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(26, "영웅격" ,"영특하고 영웅적인 기질은 있으나 지나친 우월감으로 인덕이 부족해 다른 사람들의 도움을 받지 못하며 이루고자 하는 것들을 잘 이루지 못하는 편입니다. " +
                    "사회적으로 성공하더라도 주위에 남아 있는 사람이 없어 고독이 따를 수 있습니다",
                    Suri81.EVAL_BAD),
            new Suri81(27, "중단격" ,"두뇌는 명석하나 자신의 사욕만을 채우려는 경향이 강하고 고집이세 주변 사람들의 비난을 받거나 구설수에 휘말리는 경향이 강합니다. " +
                    "자신의 이익을 위해 근시안적으로 행동하며 주변 사람들의 도움을 받기 힘들어 하고자 했던 일들이 자주 중도에 실패하게 되는 편입니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(28, "파란격" ,"평생토록 고난과 역경이 빈번하게 일어나 본인이 이루고자 하는 뜻을 펼치지 못할 가능성이 많습니다. " +
                    "일부의 목표에서는 잠시 동안 성공할 수도 있지만 결국에는 또다른 난관이 다가와 남는 것이 없으며 가족간의 관계 및 대인 관계 또한 좋지 못해 고통을 받을 수 있습니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(29, "성공격" ,"뛰어난 두뇌와 다재 다능한 역량을 갖추고 있어 큰 어려움 없이 하고자 하는 모든 일들을 이뤄내게 됩니다. " +
                    "가정운 및 건강운 또한 좋아 행복한 가정을 이루며 장수를 누리고 후세까지 그 성공이 이어지는 다복한 삶을 살아가게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(30, "불측격" ,"두뇌가 명석하고 역경을 이겨낼 수 있는 능력을 갖추고는 있으나 확실한 것이 없으며 길과 흉이 함께 존재해 얻는 것이 있으면 잃는 것도 존재하는 삶을 살아가게 됩니다. " +
                    "한가지의 성공에 일희일비 하지 말고 성공 뒤에 다가올 수 있는 역경에 대비하는 마음 가짐과 준비 자세를 지니고 살아가야 합니다. ",
                    Suri81.EVAL_NORMAL),
            new Suri81(31, "융창격" ,"지혜와 인덕 그리고 용기의 삼덕을 고루 갖추어 본인이 이루고자 하는 모든 꿈을 이루고 큰 부귀 영화를 누리게 됩니다. " +
                    "의지가 강하고 명확한 판단력을 갖춰 주위 사람들을 이끄는 지도자로 성장하며 원만한 대인관계와 화목한 가정 생활을 토대로 큰 존경을 받게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(32, "순풍격" ,"성품이 온화하고 친화력이 강해 주변 사람들과 돈독한 관계를 맺으며 그 결과 주변 사람들의 도움을 받아 큰 재물을 얻게 됩니다. " +
                    "만사가 순조롭게 풀리고 행운이 잘 따르는 편이며 가정에서는 부부간 관계가 원만해 좋은 가정을 꾸리고 일생 동안 행복하게 살아갑니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(33,"등용격" ,"주어진 일을 수행하는데 있어 강한 추진력과 탁월한 판단력을 갖추고 있으며 항상 적극적인 자세로 업무를 추진해 주변 사람들의 신망을 얻습니다. " +
                    "꾸준히 노력해 땀의 결실을 꼭 맺게 되어 큰 부와 명예를 쌓으며 그 결과가 후세까지 이어져 한 집안이 번창하게 되는 기틀을 마련합니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(34,"변란격" ,"항상 난관이 발생하며 불의의 사고가 끊이지 않아 하고자 하는 일들이 중단 되거나 단기간의 성공 뒤에 시련이 동반되는 경우가 많은 편입니다. " +
                    "성품이 원만해 타인과의 관계는 좋은 편이나 가족들과의 관계가 좋지 못하며 건강 또한 좋지 않을 가능성이 많습니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(35,"평안격" ,"매우 영민하고 온화한 성격을 갖추고 있으며 자신에게 주어진 사명을 향해 꾸준히 정진나가는 근면성을 바탕으로 큰 업적을 쌓게 됩니다 " +
                    "뛰어난 총명함과 지적인 역량은 다른 사람들의 선망의 대상이 되어 큰 칭송을 받으나 다소 내성적인 성향이 강하므로 외향적 성격을 갖추기 위해 노력해야 합니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(36,"실패격" ,"의협심과 영웅심이 강해 본인이 옳다고 생각하는 일에 적극적으로 뛰어드는 성격이나 앞뒤를 가리지 않아 항상 고생이 따르며 노력은 많이 하지만 얻는 것이 거의 없습니다. " +
                    "남을 위해 궂은 일을 도맡아 하는 성격으로 주변 사람들의 신뢰는 받는 편이지만 큰 실속은 없는 편으로 많은 인내심이 요구되는 삶을 살아가게 됩니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(37,"인덕격" ,"타인에게 의지한다기 보다는 본인 스스로 일을 처리하려는 성향이 강하며 강한 추진력을 가지고 있어 어떠한 난관도 쉽게 이겨내고 큰 성공을 이룹니다. " +
                    "인덕이 풍부하여 주위에 도와 주려는 사람들이 많으며 원만한 대인 관계를 바탕으로 주변 사람들과 더불어 행복한 삶을 살아가게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(38,"문예격" ,"명석한 두뇌와 탁월한 지혜를 갖추고 있어 자신에게 주어진 어떠한 일도 능숙하게 처리해 큰 권세와 명예를 얻게 되며 문학, 예술 등의 분야에서 특히 큰 재능이 있는 편입니다. " +
                    "가정에서도 온화한 마음 가짐과 성품으로 자식들을 잘 이끌어 항상 평안하며 행복한 가정을 꾸리고 살아가게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(39,"장성격" ,"재물운이 왕성하고 뛰어난 지혜와 인내심을 갖추고 있어 어떠한 시련도 슬기롭게 극복하고 큰 부귀영화를 누리게 됩니다. " +
                    "주변 사람들을 이끄는 통솔력이 뛰어나 본인이 속한 분야에서 지도자로서 역할을 수행하며 세상에 자신의 이름을 널리 떨치게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(40,"무상격" ,"부침이 강한 운세로 운세가 변화무쌍해 성공 후 바로 실패가 뒤따르는 등 삶의 굴곡이 심한 편입니다. " +
                    "품행이 바르지 못하고 타인과의 관계가 원만하지 못해 항상 구설수에 휘둘리는 경향이 강하고 고독한 편이므로 본인의 분수를 지키고 타인과의 관계 회복에 중점을 두는 삶을 살아가야 할 것입니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(41,"고명격" ,"강인한 의지를 갖추고 있으며 대인 관계가 좋아 뛰어난 덕망을 쌓으며 이를 바탕으로 주변 사람들을 이끄는 훌륭한 지도자로 성장해 큰 부와 명예를 이뤄냅니다. " +
                    "외모가 수려하며 건강 또한 좋아 평생토록 무병 장수하게 되며 높은 인덕으로 주위에 따르는 사람들이 끊이질 않습니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(42,"고행격" ,"모든 방면에 뛰어난 재능을 갖춘 다재 다능한 인물 이지만 추진력과 인내심이 부족하고 너무 여러 분야에 관심을 가지게 되어 중도에 일을 포기하게 되는 경향이 강합니다. " +
                    "자신의 목표를 확고히 정해 놓고 한가지 일에 전념해야 하며 인내심을 갖추고 꾸준히 노력하는 습관을 지니도록 해야 합니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(43,"미혹격" ,"두뇌가 총명하고 재능은 있지만 허영심이 많으며 자신을 포장하여 남을 속이려는 성향이 강한 편입니다. " +
                    "의지가 박약해 어려움이 닥쳤을 때 이를 극복하기 위해 노력한다기 보다는 자포자기의 심정으로 포기해 버리는 경우가 많아 큰 일을 달성하기 어렵습니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(44,"마장격" ,"사물을 새로 만들거나 어떠한 일을 기억하는 기억력은 뛰어나나 항상 시련이 끊이지 않아 하고자 하는 일들이 실속이 없고 실패만 뒤따르게 됩니다. " +
                    "가족간의 관계가 좋지 않고 건강이 나쁜 경우가 많으며 고난이 지속적으로 발생해 좌절하기 쉬우므로 강인한 의지와 심성을 갖추도록 노력해야 합니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(45,"대지격" ,"총명하며 견고한 의지를 갖추고 있어 어떠한 어려움이 있더라도 이를 굳건히 이겨내고 큰 업적을 이루게 되며 권세를 얻게 됩니다. " +
                    "명석한 두뇌와 사물을 꿰뚫어 보는 안목으로 부하 들에게 존경 받는 지도자로 성장하며 가정에서는 부부운 또한 매우 좋아 화목하고 아이들 또한 영특해 부귀영화가 지속적으로 이어지게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(46,"부지격" ,"본인의 포부와 이상이 높으나 진취적이지 못하고 인내심이 부족하여 자신이 뜻한 바를 이루지 못하는 편입니다. " +
                    "의지 박약으로 실패가 끊이지 않으며 그러한 실패로 인해 항상 고독하고 외로운 삶을 살아가게 되는 편입니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(47,"출세격" ,"다른 사람들과 비교할 수 없는 영민함과 굳건한 인내심을 바탕으로 꾸준히 노력하여 큰 부와 명예를 누리게 됩니다. " +
                    "온화한 성품으로 주변 사람들과의 관계 또한 좋으며 시련이 다가와도 동료들이 스스로 도와 역경을 극복하게 되며 본인이 쌓은 부가 대대손손 이어지게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(48,"유덕격" ,"명석한 두뇌와 뛰어난 재능 그리고 풍성한 인덕으로 타인의 존경과 칭송을 한 몸에 받습니다. " +
                    "매사에 성실한 태도와 강직한 성품으로 타의 모범이 되며 가족간의 관계도 좋아 화목한 가정 생활을 누리게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(49,"변화격" ,"뛰어난 역량과 수완을 갖추고 있어 자수 성가할 수 있으나 길과 흉이 항상 공존하고 변화가 심해 좋고 나쁨을 예측하기 힘듭니다. " +
                    "안 좋은 일이 생기더라도 반드시 좋은 일이 일어날 것이라는 믿음을 가지고 좌절하지 말고 열심히 노력해야 하며 반대로 좋은 일이 생기더라도 실패에 대해 대비하는 자세가 필요합니다. ",
                    Suri81.EVAL_NORMAL),
            new Suri81(50,"부몽격" ,"한번 성공한다면 반드시 또 한번은 실패가 따르게 되며 의지가 박약하여 잠시의 성공을 지키지 못하고 이루어 놓은 모든 것이 한 순간에 사라지고 맙니다. " +
                    "이루어 지는 것이 없이 삶이 항상 괴롭고 타인과의 관계가 단절돼 고독한 삶을 살아가게 되는 경우가 많습니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(51,"길흉격" ,"성공과 실패가 반복되는 운세로 성공이 크다면 반드시 큰 실패가 수반되는 경향이 강합니다. " +
                    "성공의 부침이 심해 안정적인 생활이 유지되기 어렵고 어렵게 얻은 부와 명예는 사라지게 되므로 항상 난관에 대비하는 자세를 갖추도록 노력해야 합니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(52,"약진격" ,"명석한 두뇌와 올바른 심성으로 어떠한 어려운 업무도 쉽게 달성하며 주어진 기회를 놓치지 않고 영민하게 처리해 크게 성공하며 부귀영화를 누리게 됩니다. " +
                    "다만 이성에 심취하는 경향이 있으므로 바른 몸가짐과 태도를 지니도록 노력해야 합니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(53,"내허격" ,"허례 허식이 강하며 자신을 치장하기만 좋아하는 성향이 크나 실제 실속은 하나도 없는 편입니다. " +
                    "유복한 가정에서 태어나 허세를 부리기에만 급급해 자신에게 다가오는 불행을 인식하지 못하고 가지고 있던 모든 것들을 잃어버리게 될 수 있으므로 겸손한 마음가짐을 지니도록 노력해야 합니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(54,"신고격" ,"강인한 의지력은 갖추고 있으나 운세가 안 좋아 하는 일마다 실패를 경험하게 되 걱정과 근심이 끊이지 않습니다. " +
                    "잠시나마 어려움을 극복했다 하더라도 또 다른 역경이 다가 오게 되어 좌절감을 맞보게 될 수 있습니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(55,"불안격" ,"외부적으로 보기에는 모든 일이 순탄히 이루어 지고 융성하게 보이나 내면적으로는 피폐하고 모래 위에 집을 지어 놓은 듯 갖은 고난과 시련이 따릅니다. " +
                    "사람들과의 관계운이 좋지 않아 이별이 자주 따르며 질병이 빈번히 발생하므로 강한 인내심과 의지력이 필요합니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(56,"빈궁격" ,"매사에 의지가 약하고 인내심이 부족하여 하고자 했던 모든 일들이 흐지부지하게 되고 실패하는 경우가 많습니다. " +
                    "열심히 노력한다 하더라고 자신의 생각데로 안되 절망할 수 있으며 가족과의 인연도 적은 편 입니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(57,"노력격" ,"굳건한 의지와 용기를 갖추고 있어 어떠한 역경도 강건하게 이겨내고 큰 성공을 이뤄내 부귀영화를 얻을 수 있습니다. " +
                    "가정운 또한 대길하여 부부의 금슬이 매우 좋고 축적한 부와 명예가 후세까지 이어집니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(58,"후영격" ,"초기에는 어려움을 겪을 수 있으나 강인한 인내심과 의지력을 바탕으로 이를 극복하고 후반으로 갈 수록 힘을 내 노력한 만큼 거둬 들이게 되는 대기만성형의 운세입니다. " +
                    "젊었을 때의 어려움이 뼈가 되고 살이되 큰 발전을 이루게 되므로 중도에 포기하지 말고 기다린다면 반드시 행복한 일들이 찾아 옵니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(59,"불우격" ,"의지가 박약하고 인내심이 부족해 시작은 창대 하나 끝은 미미한 용두사미식 결과를 얻게 되는 경향이 강합니다. " +
                    "한번의 고통을 극복해 낸다 하더라도 또 다른 고난이 다가와 좌절하기 쉬우며 가족간의 인연도 적은 편입니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(60,"암흑격" ,"앞이 안 보이는 깜깜한 어둠 속에서 본인이 가야 할 길을 찾지 못하고 이리저리 방황만 해 항상 고통과 시련이 뒤따르게 됩니다. " +
                    "반복되는 무의미한 생활의 연속이며 질병이 잦고 가족과의 인연 또한 적은 편입니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(61,"영화격" ,"두뇌가 명석하고 슬기로움을 갖추고 있어 큰 명예와 재물을 모자람 없이 갖추게 되어 평생토록 풍족한 생활을 누리게 됩니다. " +
                    "사회의 부조리에 타협하지 않는 불굴의 기상을 갖추고 있으며 노력한 만큼 결과가 따르는 운세이므로 꾸준히 노력한다면 이루고자 하는 모든 것을 이룰 수 있습니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(62,"고독격" ,"본인의 분수에 맞지 않는 행동으로 하고자 하는 일을 그르치고 실속 없는 계획만 세워 성공을 이뤄내기 힘이 듭니다. " +
                    "독단적이며 이기적인 성격으로 타인과 융화되기 힘들며 가족과 멀어질 가능성이 많고 몸과 마음이 쇠약해 병이 들기 쉽습니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(63,"길운격" ,"두뇌가 명석해 본인이 이루고자 하는 모든 꿈을 이루고 뜻하는 대로 모든 일이 풀려 일생 동안 부유한 삶을 살게 됩니다. " +
                    "어려운 상황도 일정한 시간이 지나면 저절로 극복되는 경향이 강하며 성품이 곧고 의협심이 강해 타인의 존경을 받으며 가정 또한 평안합니다. ", Suri81.EVAL_GOOD),

            new Suri81(64,"침체격" ,"본인의 타고난 역량을 제대로 발휘하지 못하며 한번 실패가 일어난다면 지속적으로 또 다른 고통이 찾아오게 됩니다. " +
                    "가족과의 이별, 근심, 질병이 끊이지 않으며 재물운도 없이 박복합니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(65,"행복격" ,"하고자 하는 모든 일들이 순탄히 풀려 큰 부귀영화를 얻게 되며 일생 동안 행복한 생활을 영위하게 됩니다. " +
                    "온화한 성품과 공과 사를 구별 할 줄 아는 사리 분별력으로 타인의 존경을 한 몸에 받으며 오랫동안 무병장수하게 됩니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(66,"역난격" ,"아둔하며 항상 계획성이 부족해 하고자 하는 일에서 늘 진척이 없이 고통만이 따르게 됩니다. " +
                    "가족간의 관계도 화목하지 못하며 질병이 빈번하게 발생해 건강이 좋지 못한 편입니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(67,"영달격" ,"뛰어난 두뇌를 바탕으로 하고자 하는 모든 일을 순조롭게 풀어 나가며 또한 운이 따라 본인의 뜻대로 모든 일을 이루고 큰 권세와 명예를 얻게 됩니다. " +
                    "성품이 강직하고 온화해 주변 사람들의 존경을 받고 따르는 사람들 또한 많은 편입니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(68,"명재격" ,"두뇌가 명석하고 창의적이며 계획에 따라 일을 진행시켜 크게 성공하여 부귀영화를 누릴 수 있습니다. " +
                    "부부간의 금슬이 좋고 가족간에 화목하며 인덕 또한 탁월해 대인 관계 역시 뛰어납니다. ",
                    Suri81.EVAL_GOOD),
            new Suri81(69,"종말격" ,"의지가 박약하고 우유부단해 어떠한 일을 처리하더라도 쉽사리 마무리 하지 못하고 중단하게 되는 경향이 많습니다. " +
                    "본인이 열심히 노력하더라도 얻는 것이 없어 불만이 쌓이며 질병과 고통이 수반됩니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(70,"적막격" ,"자신감이 없이 업무를 처리하여 어떤 일을 하더라도 실패를 맛보게 되며 항상 근심과 고통이 수반됩니다. " +
                    "본인의 성품 마저 어둡고 암울하여 주변 사람들과 교류가 적고 항상 고독한 생활을 하게 됩니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(71,"만달격" ,"인내심이 부족한 편이나 이를 극복한다면 초년의 어려움을 이겨내고 중년 이후에 어느 정도의 성공을 이뤄 낼 수 있습니다. " +
                    "꾸준히 인내하고 노력해 본인을 발전시켜야만 후반의 길운이 찾아오며 만약 그렇지 못한다면 중년 이후의 성공 조차 보장되지 못합니다. ",
                    Suri81.EVAL_NORMAL),
            new Suri81(72,"상반격" ,"일을 처리하는데 있어 결단력이 부족하며 길흉이 반복돼 한번의 성공을 이룬다 하더라도 반드시 실패가 수반되는 경향이 강합니다. " +
                    "초년에는 유복한 생활을 할 수 있느나 말년에 고난이 다가오는 경향이 강하므로 이에 대해 만반의 대비를 해 놓아야 합니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(73,"평길격" ,"타인에 비해 운은 좋으나 실천력이 부족해 평범한 삶을 살아가게 됩니다. " +
                    "일을 처리함에 있어 철저한 계획을 세우고 업무를 수행하며 자신의 분수를 지키는 삶을 살아간다면 성공에 다다를 수도 있습니다. ",
                    Suri81.EVAL_NORMAL),
            new Suri81(74,"우매격" ,"아둔하며 역량이 부족해 일을 처리해야 하는 방법을 잘 모르고 따라서 큰 성공을 거두기 어렵습니다. " +
                    "천성이 게을러 무위도식하려는 성향이 강하며 항상 궁핍하고 질병에 걸리기도 쉽습니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(75,"정수격" ,"진취적인 기상이 부족하며 성공을 거두더라도 어떠한 일련의 상황으로 인해 실패가 뒤따를 수 있습니다. " +
                    "본인의 분수에 맞는 생활을 추구하며 신중하게 업무를 추진 한다면 큰 성공은 아니더라도 일정한 수준의 성과를 이뤄낼 수는 있습니다. ",
                    Suri81.EVAL_NORMAL),
            new Suri81(76,"곤경격" ,"초년에 궁핍한 삶을 살 가능성이 많으며 하는 일에 실패가 잦아 좌절할 가능성이 큽니다. " +
                    "가족끼리의 인연 또한 별로 없으나 다만 말년으로 갈수록 운이 좋아 질 가능성도 존재합니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(77,"전후격" ,"타고난 의지가 약해 남에게 의지하는 성향이 강하며 어떠한 일을 시작하던지 간에 마무리 없이 끝나 버리는 경우가 많습니다. " +
                    "초년에는 부모의 덕으로 유복한 생활을 할 수 있으나 말년으로 갈 수록 고통이 수반되며 주변 사람들과의 관계를 돈독히 한다면 본인을 도와주는 귀인을 만날 역경을 극복 할 수도 있습니다. ",
                    Suri81.EVAL_NORMAL),
            new Suri81(78,"선길격" ,"두뇌가 명석하고 재능이 뛰어나 본인의 노력 여하에 따라 초반에 성공하여 부귀영화를 누릴 수 있으나 후반으로 갈 수록 능력이 쇠퇴하여 고통이 따르게 됩니다. " +
                    "대인 관계를 돈독히 하며 말년의 삶을 위해 중년까지 쌓아 놓은 재물을 잘 관리할 필요성이 있습니다. ",
                    Suri81.EVAL_NORMAL),
            new Suri81(79,"종극격" ,"자신이 무엇인가를 이루겠다는 목표를 지니고 열심히 노력한다 해도 이루는 것이 별로 없어 본인의 기력만 허비하게 됩니다. " +
                    "노력에 비해 얻는 것이 적어 몸과 마음이 모두 쇠약해 지고 주변 사람들로부터 신뢰도 얻지 못합니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(80,"종결격" ,"편협한 생각을 갖고 있어 큰 뜻을 세우기 어렵고 일생 동안 고통이 뒤따라 몸과 마음이 피폐해 집니다. " +
                    "자존심이 매우 세며 타인과 잘 융화되지 못하는 성격으로 사회 생활을 하는데 있어 어려움을 겪을 수 있습니다. ",
                    Suri81.EVAL_BAD),
            new Suri81(81,"환원격" ,"무엇을 하든지 크게 발전할 수 있으며 실패를 한다고 하더라도 강인한 의지와 인내심을 바탕으로 이를 극복하고 반드시 성공의 길로 들어 설 수 있습니다. " +
                    "항상 행운이 함께해 특별한 노력이 없이도 부와 명예가 동반된 삶을 살 수 있습니다. ",
                    Suri81.EVAL_GOOD),
    };

    public Suri81 getSuri(int suri) {
        for (int i = 0; i < suri81s.length; i++) {
            if (suri81s[i].getSuri() == suri)
                return suri81s[i];
        }

        return suri81s[0];
    }


    // 생성자
    public DBManager(Context context) {
        this.context = context;
        DB_PATH = "/data/data/" + context.getApplicationContext().getPackageName()+"/databases/";

        Log.d("OwnNameGen", "DB_PATH = " + DB_PATH);

        try {
            copydatabase(DB_NAME_CH);
            copydatabase(DB_NAME_MANSE);
        } catch (IOException e) {
            Log.d("OwnNameGen", e.toString());
        }

        File f = new File(DB_PATH + DB_NAME_CH);

        //Toast.makeText(context, "DB_PATH = "+ DB_PATH + " e = " +f.exists(), Toast.LENGTH_LONG).show();

        this.opener_ch = new OpenHelper(context, DB_NAME_CH, null, dbVersion);
        this.db_ch = opener_ch.getWritableDatabase();
        this.opener_manse = new OpenHelper(context, DB_NAME_MANSE, null, dbVersion);
        this.db_manse = opener_manse.getReadableDatabase();

    }

    // Opener of DB and Table
    private class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory,
                          int version) {
            super(context, name, null, version);
            // TODO Auto-generated constructor stub

        }

        // 생성된 DB가 없을 경우에 한번만 호출됨
        @Override
        public void onCreate(SQLiteDatabase arg0) {
            // String dropSql = "drop table if exists " + tableName;
            // db.execSQL(dropSql);

            /*
            String createSql = "create table " + tableName + " ("
                    + "id integer primary key autoincrement, " + "SSID text, "
                    + "capabilities integer, " + "passwd text)";
            arg0.execSQL(createSql);
            Toast.makeText(context, "DB is opened", 0).show();
            */
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
        }
    }
    // Data 읽기(꺼내오기)
    public void selectData(int index){

        String sql = "select * from " + DB_CH_TABLE_NAME + " where No = "+index+";";
        Cursor result = db_ch.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            int no = result.getInt(0);
            String hanja = result.getString(2);

            Log.d("OwnNameGen", "No = "+ no + " hanja = " + hanja);
        }
        result.close();

        sql = "select * from " + DB_MANSE_TABLE_NAME + " where cd_no = "+index+";";
        result = db_manse.rawQuery(sql, null);
        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            int no = result.getInt(0);
            String hanja = result.getString(9);

            Log.d("OwnNameGen", "No = "+ no + " hanja = " + hanja);
        }
        result.close();

    }

    public ArrayList<String> getFamilyNameKr(String familyNameKr) {

        String sql = "select * from " + DB_CH_TABLE_NAME + " where Hangul = '"+ familyNameKr+"' and Position = 2;";
        ArrayList<String> resultArrayList = new ArrayList<String>();
        Cursor result = db_ch.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            while (!result.isAfterLast()){
                String resultEntry = result.getString(1).toString();
                if (!resultArrayList.contains(resultEntry)) {
                    resultArrayList.add(resultEntry);
                }
                Log.d(TAG, result.getString(1));

                result.moveToNext();
            }
        }
        result.close();
        return  resultArrayList;
    }

    public ArrayList<Letter> getFamilyNameInfo(String familyNameKr) {

        String sql = "select * from " + DB_CH_TABLE_NAME + " where Hangul = '"+ familyNameKr+"' and Position = 2;";
        ArrayList<Letter> resultArrayList = new ArrayList<Letter>();
        Cursor result = db_ch.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            while (!result.isAfterLast()) {
                String familyNameKrGot = result.getString(1).toString();
                String familyNameCh = result.getString(2).toString();
                String familyNameMean = result.getString(3).toString();
                Letter familyNameLetter = new Letter(familyNameKrGot, familyNameCh, familyNameMean);
                resultArrayList.add(familyNameLetter);
                result.moveToNext();
            }
        }
        result.close();
        return  resultArrayList;
    }

    public ArrayList<Letter> getLetter(String nameKr) {

        String sql = "select * from " + DB_CH_TABLE_NAME + " where Hangul = '"+ nameKr +"' and Position = 1;";
        ArrayList<Letter> resultArrayList = new ArrayList<Letter>();
        Cursor result = db_ch.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            while (!result.isAfterLast()) {
                String familyNameKrGot = result.getString(1).toString();
                String familyNameCh = result.getString(2).toString();
                String familyNameMean = result.getString(3).toString();
                Letter familyNameLetter = new Letter(familyNameKrGot, familyNameCh, familyNameMean);
                resultArrayList.add(familyNameLetter);
                result.moveToNext();
            }
        }
        result.close();
        return  resultArrayList;
    }

    public Letter getLetterFromCh(String familyNameCh, int type) {
        String sql = "select * from " + DB_CH_TABLE_NAME + " where Hanja = '"+ familyNameCh+"' and Position = " + type +";";
        Letter familyNameLetter = new Letter();
        Cursor result = db_ch.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
                String familyNameKr = result.getString(1).toString();
                String familyNameChGot = result.getString(2).toString();
                String familyNameMean = result.getString(3).toString();
                familyNameLetter = new Letter(familyNameKr, familyNameChGot, familyNameMean);
                familyNameLetter.setKrLetter(familyNameKr);
                familyNameLetter.setChLetter(familyNameChGot);
                familyNameLetter.setMean(familyNameMean);
                familyNameLetter.setStroke(result.getInt(4));
                familyNameLetter.setProFiveElement(result.getString(5).toString());
                familyNameLetter.setRadFiveElement(result.getString(6).toString());
                familyNameLetter.setLetter(true);
        }
        result.close();
        return  familyNameLetter;
    }

    public String[] getSajuYearMonthDay(String birthYear, String birthMonth, String birthDay) {
        int year = new Integer(birthYear);
        String[] returnStringArray = new String[4];

        //String sql = "select * from " + DB_MANSE_TABLE_NAME + " where cd_sy = '"+ familyNameCh+"' and cd_sm = " + type +";";
        String sql = "select * from " + DB_MANSE_TABLE_NAME + " where cd_sy = "+ year +" and cd_sm = '" + birthMonth + "' and cd_sd = '" + birthDay +"';";
        Cursor result = db_manse.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            returnStringArray[0] = result.getString(9);
            returnStringArray[1] = result.getString(11);
            returnStringArray[2] = result.getString(13);
        }
        result.close();
        return returnStringArray;
    }

    public ArrayList<String> getNamesFullList(int name_len) {
        ArrayList<String> resultList = new ArrayList<String>();
        ArrayList<String> allKoreanLetters = new ArrayList<String>();
        String name = new String();
        int size;

        String sql = "select * from " + DB_CH_TABLE_NAME + " where Position = 1;";
        Cursor result = db_ch.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            while (!result.isAfterLast()) {
                String familyNameKr = result.getString(1).toString();
                if(!allKoreanLetters.contains(familyNameKr))
                    allKoreanLetters.add(familyNameKr);
                result.moveToNext();
            }
        }

        size = allKoreanLetters.size();
        for (int i = 0; i < size; i++) {
            String eachName1 = allKoreanLetters.get(i);
            if (name_len > 1 ) {
                for (int j = 0; j < size; j++) {
                    String eachName2 = eachName1.substring(0, 1)  + allKoreanLetters.get(j);
                    if (name_len > 2 ) {
                        for (int k = 0; k < size; k++) {
                            String eachName3 = eachName2.substring(0, 2) + allKoreanLetters.get(k);
                            resultList.add(eachName3);
                        }
                    } else {
                        resultList.add(eachName2);
                    }
                }
            } else {
                resultList.add(eachName1);
            }
        }
        result.close();
        return resultList;
    }

    /*
    // 데이터 추가
    public void insertData(APinfo info) {
        String sql = "insert into " + tableName + " values(NULL, '"
                + info.getSSID() + "', " + info.getCapabilities() + ", '"
                + info.getPasswd() + "');";
        db.execSQL(sql);
    }

    // 데이터 갱신
    public void updateData(APinfo info, int index) {
        String sql = "update " + tableName + " set SSID = '" + info.getSSID()
                + "', capabilities = " + info.getCapabilities()
                + ", passwd = '" + info.getPasswd() + "' where id = " + index
                + ";";
        db.execSQL(sql);
    }

    // 데이터 삭제
    public void removeData(int index) {
        String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);
    }

    // 데이터 검색
    public APinfo selectData(int index) {
        String sql = "select * from " + tableName + " where id = " + index
                + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            APinfo info = new APinfo(result.getInt(0), result.getString(1),
                    result.getInt(2), result.getString(3));
            result.close();
            return info;
        }
        result.close();
        return null;
    }

    // 데이터 전체 검색
    public ArrayList<apinfo> selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();
        ArrayList<apinfo> infos = new ArrayList<apinfo>();

        while (!results.isAfterLast()) {
            APinfo info = new APinfo(results.getInt(0), results.getString(1),
                    results.getInt(2), results.getString(3));
            infos.add(info);
            results.moveToNext();
        }
        results.close();
        return infos;
    }
    */
    private void copydatabase(String dbName) throws IOException {

        //Open your local db as the input stream
        InputStream myinput = this.context.getAssets().open(dbName);
        // Path to the just created empty db
        String outfilename = DB_PATH + dbName;
        File outfile = new File(outfilename);

        if (!outfile.exists()) {
            outfile.createNewFile();
        }

        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(outfilename);
        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer))>0)
        {
            myoutput.write(buffer,0,length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

}
