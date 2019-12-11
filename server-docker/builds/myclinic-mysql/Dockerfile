FROM mysql:5.7
COPY start.sh /root/start.sh
COPY myclinic-entrypoint.sh /myclinic-entrypoint.sh
RUN chmod 775 /myclinic-entrypoint.sh \
    && chmod 775 /root/start.sh
ENTRYPOINT []
CMD ["/bin/bash", "/root/start.sh"]
