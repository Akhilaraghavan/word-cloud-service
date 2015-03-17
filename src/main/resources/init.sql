create table word_cloud(
	event_time timestamp,
	word_key text,
	word text,
	word_count int,
	primary key(event_time,word_key)
) with clustering order by word_key desc