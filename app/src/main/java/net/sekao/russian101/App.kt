package net.sekao.russian101

import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.Typeface.*
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import trikita.anvil.Anvil
import trikita.anvil.BaseDSL
import trikita.anvil.RenderableView
import trikita.anvil.DSL.*
import trikita.anvil.RenderableAdapter

data class Lesson(val title: String, val subtitle: String, val pageCount: Int)

val LESSONS = arrayOf(
    Lesson("Alphabet", "алфавит", 35),
    Lesson("Meeting People", "Знакомство", 9),
    Lesson("Family", "семья", 8),
    Lesson("Where do you work?", "Где вы работаете?", 13),
    Lesson("Where do you live?", "Где вы живете?", 8),
    Lesson("Shopping", "покупки", 27),
    Lesson("In the restaurant", "В ресторане", 23),
    Lesson("Transportation", "транспорт", 18),
    Lesson("In the hotel", "В гостинице", 18),
    Lesson("The telephone", "телефон", 24)
)

const val LESSON_NUM = "lesson"
const val PAGE_NUM = "page"
const val BACKGROUND_COLOR = "#005B98"
const val BACKGROUND_COLOR_ALPHA = "#55005b98"

class Page(c: Context) : RenderableView(c) {
    var currentPage = 0
    var pageAdapter: PagerAdapter? = null

    constructor(c: Context, lessonNum: Int, pageNum: Int) : this(c) {
        currentPage = pageNum
        val pageCount = LESSONS[lessonNum].pageCount
        pageAdapter = object : PagerAdapter() {
            override fun isViewFromObject(p0: View, p1: Any): Boolean {
                return p0 == p1
            }

            override fun getCount(): Int {
                return pageCount
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val v = ImageView(c)
                v.setImageBitmap(BitmapFactory.decodeStream(
                    c.assets.open(
                        "lesson" + (lessonNum + 1) + "/" + (position + 1) + ".webp"
                    )
                ))
                container.addView(v)
                return v
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                if (`object` is View) {
                    container.removeView(`object`)
                }
            }

            override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
                if (currentPage == position) {
                    return
                }
                currentPage = position
            }
        }
    }

    override fun view() {
        linearLayout {
            size(FILL, FILL)
            backgroundColor(Color.parseColor(BACKGROUND_COLOR))
            v(ViewPager::class.java) {
                init {
                    val v = Anvil.currentView<ViewPager>()
                    v.adapter = this.pageAdapter
                    v.currentItem = this.currentPage
                }
            }
        }
    }
}

class PageActivity() : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lessonNum = this.intent.extras!!.getInt(LESSON_NUM)
        val pageNum = this.intent.extras!!.getInt(PAGE_NUM)
        setContentView(Page(this, lessonNum, pageNum))
    }
}

class LessonDetail(c: Context) : RenderableView(c) {
    var currentLesson: Int = 0
    var gridAdapter: RenderableAdapter? = null
    var isTablet = false

    constructor (c: Context, lessonNum: Int) : this(c) {
        setLessonNum(c, lessonNum)
    }

    fun setLessonNum (c: Context, lessonNum: Int) {
        this.currentLesson = lessonNum
        val pageCount = LESSONS[lessonNum].pageCount
        this.gridAdapter = RenderableAdapter.withItems(0.rangeTo(pageCount-1).toMutableList()) {
            pos, value ->
            imageView {
                imageBitmap(
                    BitmapFactory.decodeStream(
                        c.assets.open(
                            "lesson" + (lessonNum + 1) + "/" + (pos + 1) + "t.webp"
                        )
                    )
                )
            }
        }
        Anvil.render(this)
    }

    override fun view() {
        val spacing = (10 * resources.displayMetrics.density).toInt()

        gridView {
            size(FILL, FILL)
            backgroundColor(Color.parseColor(if (isTablet) BACKGROUND_COLOR_ALPHA else BACKGROUND_COLOR))
            padding((5 * resources.displayMetrics.density).toInt())
            columnWidth((90 * resources.displayMetrics.density).toInt())
            numColumns(GridView.AUTO_FIT)
            verticalSpacing(spacing)
            horizontalSpacing(spacing)
            stretchMode(GridView.STRETCH_COLUMN_WIDTH)
            adapter(gridAdapter)
            onItemClick { parent, view, pos, id ->
                run {
                    val intent = Intent(context, PageActivity::class.java)
                    intent.putExtra(LESSON_NUM, this.currentLesson)
                    intent.putExtra(PAGE_NUM, pos)
                    context.startActivity(intent)
                }
            }
        }
    }
}

class LessonDetailActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lessonNum = this.intent.extras!!.getInt(LESSON_NUM)
        setContentView(LessonDetail(this, lessonNum))
    }
}

class LessonList(c: Context) : RenderableView(c) {
    var listAdapter = RenderableAdapter.withItems(LESSONS.toMutableList()) { pos, value ->
        linearLayout {
            size(WRAP, WRAP)
            orientation(LinearLayout.VERTICAL)
            padding((5 * resources.displayMetrics.density).toInt())
            textView {
                size(MATCH, WRAP)
                textSize(24 * resources.displayMetrics.density)
                typeface(Typeface.create(SANS_SERIF, BOLD))
                textColor(Color.WHITE)
                text(value.title)
            }
            textView {
                size(MATCH, WRAP)
                textSize(14 * resources.displayMetrics.density)
                typeface(Typeface.create(SANS_SERIF, NORMAL))
                textColor(Color.WHITE)
                text(value.subtitle)
            }
        }
    }
    var selectedLesson = 0

    override fun view() {
        val isTablet = resources.displayMetrics.widthPixels / resources.displayMetrics.density >= 900
        val tabletWidth = (300 * resources.displayMetrics.density).toInt()
        val outerMargin = (16 * resources.displayMetrics.density).toInt()

        relativeLayout {
            backgroundColor(Color.parseColor(BACKGROUND_COLOR))
            frameLayout {
                size(MATCH, MATCH)
                imageView {
                    imageResource(R.drawable.background)
                    scaleType(ImageView.ScaleType.CENTER_CROP)
                }
            }
            linearLayout {
                size(MATCH, MATCH)
                gravity(BaseDSL.TOP)
                orientation(LinearLayout.VERTICAL)
                margin(outerMargin, 0, outerMargin, 0)
                textView {
                    textSize(48 * resources.displayMetrics.density)
                    typeface(Typeface.create(SANS_SERIF, BOLD))
                    textColor(Color.WHITE)
                    gravity(BaseDSL.CENTER_HORIZONTAL)
                    text(R.string.app_name)
                }
                linearLayout {
                    size(FILL, FILL)
                    orientation(LinearLayout.HORIZONTAL)
                    listView {
                        backgroundColor(Color.parseColor(BACKGROUND_COLOR_ALPHA))
                        size(if (isTablet) tabletWidth else FILL, FILL)
                        itemsCanFocus(true)
                        onItemClick { parent, view, pos, id ->
                            run {
                                if (isTablet) {
                                    this.selectedLesson = pos
                                }
                                else {
                                    val intent = Intent(context, LessonDetailActivity::class.java)
                                    intent.putExtra(LESSON_NUM, pos)
                                    context.startActivity(intent)
                                }
                            }
                        }
                        adapter(listAdapter)
                    }
                    if (isTablet) {
                        v(LessonDetail::class.java) {
                            val v = Anvil.currentView<LessonDetail>()
                            v.setLessonNum(this.context, this.selectedLesson)
                            v.isTablet = true
                        }
                    }
                }
            }
        }
    }
}

class LessonListActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LessonList(this))
    }
}
