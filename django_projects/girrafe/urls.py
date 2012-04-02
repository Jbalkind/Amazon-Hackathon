from django.conf.urls.defaults import patterns, include, url
import settings

from girrafe.data.views import twitterJSON, bloggerJSON, youtubeJSON, amazonJSON

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'girrafe.views.home', name='home'),
    # url(r'^girrafe/', include('girrafe.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    url(r'^admin/', include(admin.site.urls)),
	(r'^media/(?P<path>.*)$', 'django.views.static.serve', {'document_root': settings.MEDIA_ROOT}),
    
    url(r'^data/twitter/(.+)/$', twitterJSON),
    url(r'^data/blogger/(.+)/$', bloggerJSON),
    url(r'^data/amazon/(.+)/$', amazonJSON),
    url(r'^data/youtube/(.+)/$', youtubeJSON)
)
