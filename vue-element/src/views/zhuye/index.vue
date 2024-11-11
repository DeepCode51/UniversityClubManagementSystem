<template>
  <div>
    <top />
    <!-- ***** Header Area Start ***** -->

    <!-- ***** Header Area End ***** -->
    <navs />
    <!-- ***** Main Banner Area Start ***** -->
    <!-- ***** Main Banner Area Start ***** -->
    <section id="top" class="section main-banner" data-section="section1">
      <video id="bg-video" autoplay muted loop>
        <source src="@/assets/images/course-video.mp4" type="video/mp4">
      </video>

      <div class="video-overlay header-text">
        <div class="container">
          <div class="row">
            <div class="col-lg-12">
              <div class="caption">
                <h6>你好学生</h6>
                <h2>欢迎来到社团招新</h2>
                <p>体验大学丰富生活，参与社团活动</p>
                <div class="main-button-red">
                  <div><a href="#/teamlist">加入我们！</a></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <foot />

  </div>
</template>

<script>
import top from '../qdutils/top/top'
import navs from '../qdutils/navs/index'
import foot from '../qdutils/footer/index'
export default {
  name: 'Index',
  components: { top, navs, foot }
}
</script>

<style scoped>

@import "../../vendor/bootstrap/css/bootstrap.min.css";
@import "../../assets/css/templatemo-edu-meeting.css";
</style>
