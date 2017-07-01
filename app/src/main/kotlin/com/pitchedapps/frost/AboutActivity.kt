package com.pitchedapps.frost

import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import ca.allanwang.kau.about.AboutActivityBase
import ca.allanwang.kau.adapters.FastItemThemedAdapter
import ca.allanwang.kau.adapters.ThemableIItem
import ca.allanwang.kau.adapters.ThemableIItemDelegate
import ca.allanwang.kau.iitems.LibraryIItem
import ca.allanwang.kau.utils.*
import ca.allanwang.kau.views.createSimpleRippleDrawable
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.typeface.IIcon
import com.pitchedapps.frost.utils.Prefs


/**
 * Created by Allan Wang on 2017-06-26.
 */
class AboutActivity : AboutActivityBase(R.string::class.java, configBuilder = {
    textColor = Prefs.textColor
    accentColor = Prefs.accentColor
    backgroundColor = Prefs.bgColor.withMinAlpha(200)
    cutoutForeground = if (0xff3b5998.toInt().isColorVisibleOn(Prefs.bgColor)) 0xff3b5998.toInt() else Prefs.accentColor
    cutoutDrawableRes = R.drawable.frost_f_256
}) {

    override fun getLibraries(libs: Libs): List<Library> {
        val include = arrayOf(
                "materialdialogs",
                "kotterknife",
                "glide",
                "jsoup"
        )
        /*
         * These are great libraries, but either aren't used directly or are too common to be listed
         * Give more emphasis on the unique libs!
         */
        val exclude = arrayOf(
                "GoogleMaterialDesignIcons",
                "intellijannotations",
                "MaterialDesignIconicIcons",
                "MaterialDesignIcons",
                "materialize",
                "appcompat_v7",
                "design",
                "recyclerview_v7",
                "support_v4"
        )
        val l = libs.prepareLibraries(this, include, exclude, true, true)
//        l.forEach { KL.d("Lib ${it.definedName}") }
        return l
    }

    override fun postInflateMainPage(adapter: FastItemThemedAdapter<IItem<*, *>>) {
        /**
         * Frost may not be a library but we're conveying the same info
         */
        val frost = Library().apply {
            libraryName = string(R.string.app_name)
            author = "Pitched Apps"
            libraryWebsite = "https://github.com/AllanWang/Frost-for-Facebook"
            isOpenSource = true
            libraryDescription = string(R.string.frost_description)
            libraryVersion = BuildConfig.VERSION_NAME
            license = License().apply {
                licenseName = "GNU GPL v3"
                licenseWebsite = "https://www.gnu.org/licenses/gpl-3.0.en.html"
            }
        }
        adapter.add(LibraryIItem(frost)).add(AboutLinks())

    }

    class AboutLinks : AbstractItem<AboutLinks, AboutLinks.ViewHolder>(), ThemableIItem by ThemableIItemDelegate() {
        override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

        override fun getType(): Int = R.id.item_about_links

        override fun getLayoutRes(): Int = R.layout.item_about_links

        override fun bindView(holder: ViewHolder, payloads: MutableList<Any>?) {
            super.bindView(holder, payloads)
            with(holder) {
                bindIconColor(rate, github)
                bindBackgroundColor(container)
            }
        }

        fun theme(vararg images: ImageView) {
            val ripple = createSimpleRippleDrawable(accentColor!!, Color.TRANSPARENT)
            images.forEach {
                it.background = ripple
                it.drawable.setTint(textColor!!)
            }
        }

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

            val container: ConstraintLayout by bindView(R.id.about_icons_container)
            val rate: ImageView by bindView(R.id.about_rate)
            val github: ImageView by bindView(R.id.about_github)

            init {
                val c = itemView.context
                setup(rate, GoogleMaterial.Icon.gmd_star, { c.startPlayStoreLink(R.string.play_store_package_id) })
                setup(github, CommunityMaterial.Icon.cmd_github_circle, { c.startLink("https://github.com/AllanWang/Frost-for-Facebook") })
            }

            fun setup(image: ImageView, icon: IIcon, onClick: () -> Unit) {
                image.setImageDrawable(icon.toDrawable(itemView.context, 32))
                image.setOnClickListener({ onClick() })
            }

        }
    }
}